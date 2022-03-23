package banking.account.service.service;

import banking.account.service.domain.input.AccountDepositInput;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.PrivateLoanAccount;
import banking.account.service.domain.SavingsAccount;
import banking.account.service.exception.AccountLockedException;
import banking.account.service.repository.AccountRepository;
import banking.account.service.service.deposit.DepositServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class DepositServiceTest {

  private static final String IBAN = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";
  private static final BigDecimal AMOUNT = new BigDecimal("200");

  @InjectMocks
  private DepositServiceImpl underTest;
  @Mock
  private AccountHistoryServiceImpl accountHistoryServiceMock;
  @Mock
  private AccountRepository accountRepositoryMock;
  @Mock
  private AccountLoaderService accountLoaderServiceMock;

  @BeforeEach
  public void setUp() {
    openMocks(this);
  }

  @Test
  public void depositChecking() {
    CheckingAccount account = new CheckingAccount(IBAN);
    when(accountLoaderServiceMock.loadAccountByIban(anyString())).thenReturn(account);

    underTest.deposit(generateTransaction());

    verify(accountHistoryServiceMock).updateAccountHistoryForDeposit(account, AMOUNT);
    verify(accountLoaderServiceMock).loadAccountByIban(IBAN);
    verify(accountRepositoryMock).save(account);

    assertEquals(account.getBalance(), AMOUNT);
  }

  @Test
  public void depositSaving() {
    SavingsAccount account = new SavingsAccount(IBAN, IBAN_2);
    when(accountLoaderServiceMock.loadAccountByIban(anyString())).thenReturn(account);

    underTest.deposit(generateTransaction());

    verify(accountHistoryServiceMock).updateAccountHistoryForDeposit(account, AMOUNT);
    verify(accountLoaderServiceMock).loadAccountByIban(IBAN);
    verify(accountRepositoryMock).save(account);

    assertEquals(account.getBalance(), AMOUNT);
  }

  @Test
  public void depositPrivatLoan() {
    PrivateLoanAccount account = new PrivateLoanAccount(IBAN);
    when(accountLoaderServiceMock.loadAccountByIban(anyString())).thenReturn(account);

    underTest.deposit(generateTransaction());

    verify(accountHistoryServiceMock).updateAccountHistoryForDeposit(account, AMOUNT);
    verify(accountLoaderServiceMock).loadAccountByIban(IBAN);
    verify(accountRepositoryMock).save(account);

    assertEquals(account.getBalance(), AMOUNT);
  }

  @Test
  public void depositLockedAccount() {
    CheckingAccount account = new CheckingAccount(IBAN);
    account.setLocked(true);
    when(accountLoaderServiceMock.loadAccountByIban(anyString())).thenReturn(account);

    Assertions.assertThrows(AccountLockedException.class, () ->
        underTest.deposit(generateTransaction()));
  }

  private AccountDepositInput generateTransaction() {
    return new AccountDepositInput(IBAN, AMOUNT);
  }
}
