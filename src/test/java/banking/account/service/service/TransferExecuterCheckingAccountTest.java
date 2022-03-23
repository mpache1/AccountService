package banking.account.service.service;

import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.PrivateLoanAccount;
import banking.account.service.service.transfer.TransferExecuterCheckingAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class TransferExecuterCheckingAccountTest {

  private static final BigDecimal AMOUNT = new BigDecimal("100.00");
  private static final String IBAN_1 = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";

  @InjectMocks
  TransferExecuterCheckingAccount underTest;
  @Mock
  private AccountHistoryService accountHistoryService;

  @BeforeEach
  public void setUp() {
    openMocks(this);
  }

  @Test
  public void testTransfer() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    PrivateLoanAccount privateLoanAccount = new PrivateLoanAccount(IBAN_2);

    boolean result = underTest.transfer(checkingAccount, privateLoanAccount, AMOUNT);

    verify(accountHistoryService).updateAccountHistorysForTransfer(checkingAccount, privateLoanAccount, AMOUNT);
    assertTrue(result);
  }
}
