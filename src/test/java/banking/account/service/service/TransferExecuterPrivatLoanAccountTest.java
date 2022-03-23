package banking.account.service.service;

import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.PrivateLoanAccount;
import banking.account.service.service.transfer.TransferExecuterPrivatLoanAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.MockitoAnnotations.openMocks;

class TransferExecuterPrivatLoanAccountTest {

  private static final BigDecimal AMOUNT = new BigDecimal("100.00");
  private static final String IBAN_1 = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";

  @InjectMocks
  TransferExecuterPrivatLoanAccount underTest;

  @BeforeEach
  public void setUp() {
    openMocks(this);
  }

  @Test
  public void testTransfer() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    PrivateLoanAccount privateLoanAccount = new PrivateLoanAccount(IBAN_2);

    boolean result = underTest.transfer(privateLoanAccount, checkingAccount, AMOUNT);

    assertFalse(result);
  }
}