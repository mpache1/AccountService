package banking.account.service.service;

import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountHistoryServiceTest {

  private static final String IBAN_1 = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";
  private static final BigDecimal AMOUNT = new BigDecimal("100.00");

  private AccountHistoryService underTest;

  @BeforeEach
  public void setUp() {
    underTest = new AccountHistoryServiceImpl();
  }

  @Test
  void updateAccountHistoryForDeposit() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    String expected = "+" + formatBalanceForOutput(AMOUNT) + " has been deposited";

    underTest.updateAccountHistoryForDeposit(checkingAccount, AMOUNT);

    assertEquals(checkingAccount.getAccountHistory().size(), 1);
    assertEquals(expected, checkingAccount.getAccountHistory().get(0));
  }

  @Test
  void updateAccountHistorysForTransfer() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN_2, IBAN_1);
    String formatedAmount = formatBalanceForOutput(AMOUNT);

    String expected1 = "-" + formatedAmount + " transfered to " + IBAN_2;
    String expected2 = "+" + formatedAmount + " transfered from " + IBAN_1;

    underTest.updateAccountHistorysForTransfer(checkingAccount, savingsAccount, AMOUNT);

    assertEquals(checkingAccount.getAccountHistory().size(), 1);
    assertEquals(expected1, checkingAccount.getAccountHistory().get(0));
    assertEquals(savingsAccount.getAccountHistory().size(), 1);
    assertEquals(expected2, savingsAccount.getAccountHistory().get(0));
  }
}