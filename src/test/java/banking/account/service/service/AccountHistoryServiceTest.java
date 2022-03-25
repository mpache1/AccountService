package banking.account.service.service;

import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountHistoryServiceTest {

  private static final String IBAN_1 = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";
  private static final BigDecimal AMOUNT = new BigDecimal("100.00");

  @InjectMocks
  private AccountHistoryServiceImpl underTest;
  @Mock
  private MessageSource messageSourceMock;
  @Mock
  private TimeStampService timeStampServiceMock;

  @BeforeEach
  public void setUp() {
    openMocks(this);
  }

  @Test
  void updateAccountHistoryForDeposit() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    String expected = "25.03.2022-21:26 +" + formatBalanceForOutput(AMOUNT) + " has been deposited";

    when(timeStampServiceMock.generateTimeStamp()).thenReturn("25.03.2022-21:26");
    when(messageSourceMock.getMessage(eq("account.histoty.deposit"), any(), any())).thenReturn(expected);

    underTest.updateAccountHistoryForDeposit(checkingAccount, AMOUNT);

    verify(messageSourceMock).getMessage(same("account.histoty.deposit"), any(), any());

    assertEquals(checkingAccount.getAccountHistory().size(), 1);
    assertEquals(expected, checkingAccount.getAccountHistory().get(0));
  }

  @Test
  void updateAccountHistorysForTransfer() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN_2, IBAN_1);
    String formatedAmount = formatBalanceForOutput(AMOUNT);

    String expected1 = "25.03.2022-21:26 -" + formatedAmount + " transfered to " + IBAN_2;
    String expected2 = "25.03.2022-21:26 +" + formatedAmount + " transfered from " + IBAN_1;

    when(timeStampServiceMock.generateTimeStamp()).thenReturn("25.03.2022-21:26");
    when(messageSourceMock.getMessage(eq("account.history.transfer.to"), any(), any())).thenReturn(expected1);
    when(messageSourceMock.getMessage(eq("account.history.transfer.from"), any(), any())).thenReturn(expected2);

    underTest.updateAccountHistorysForTransfer(checkingAccount, savingsAccount, AMOUNT);

    verify(messageSourceMock).getMessage(same("account.history.transfer.to"), any(), any());
    verify(messageSourceMock).getMessage(same("account.history.transfer.from"), any(), any());

    assertEquals(checkingAccount.getAccountHistory().size(), 1);
    assertEquals(expected1, checkingAccount.getAccountHistory().get(0));
    assertEquals(savingsAccount.getAccountHistory().size(), 1);
    assertEquals(expected2, savingsAccount.getAccountHistory().get(0));
  }
}