package banking.account.service.service;

import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.SavingsAccount;
import banking.account.service.service.AccountHistoryService;
import banking.account.service.service.transfer.TransferExecuterSavingAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class TransferExecuterSavingAccountTest {

  private static final BigDecimal AMOUNT = new BigDecimal("100.00");
  private static final String IBAN_1 = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";

  @InjectMocks
  TransferExecuterSavingAccount underTest;
  @Mock
  private AccountHistoryService accountHistoryService;

  @BeforeEach
  public void setUp() {
    openMocks(this);
  }

  @Test
  public void transfer() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN_2, IBAN_1);

    boolean result = underTest.transfer(savingsAccount, checkingAccount, AMOUNT);

    verify(accountHistoryService).updateAccountHistorysForTransfer(savingsAccount, checkingAccount, AMOUNT);

    assertTrue(result);
  }

  @Test
  public void transferFails() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN_2, "TEST_IBAN");

    boolean result = underTest.transfer(savingsAccount, checkingAccount, AMOUNT);

    assertFalse(result);
  }
}