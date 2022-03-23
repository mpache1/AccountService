package banking.account.service.service;

import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.SavingsAccount;
import banking.account.service.domain.input.AccountTransferInput;
import banking.account.service.exception.AccountLockedException;
import banking.account.service.repository.AccountRepository;
import banking.account.service.service.transfer.TransferExecuterCheckingAccount;
import banking.account.service.service.transfer.TransferServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class TransferServiceTest {

  private static final String SENDING_IBAN = "DE90123456781234567890";
  private static final String RECEIVING_IBAN = "DE90123456781234567891";
  private static final BigDecimal AMOUNT = new BigDecimal("200");

  @InjectMocks
  private TransferServiceImpl underTest;
  @Mock
  private AccountRepository accountRepositoryMock;
  @Mock
  private AccountLoaderService accountLoaderServiceMock;
  @Mock
  private TransferExecuterCheckingAccount transferExecuterMock;

  @BeforeEach
  public void setup() {
    openMocks(this);
  }

  @Test
  public void transferChecking() {
    AccountTransferInput accountInput = new AccountTransferInput(SENDING_IBAN, RECEIVING_IBAN, AMOUNT);
    CheckingAccount checkingAccount = new CheckingAccount(SENDING_IBAN);
    SavingsAccount savingsAccount = new SavingsAccount(RECEIVING_IBAN, SENDING_IBAN);

    when(accountLoaderServiceMock.loadAccountByIban(SENDING_IBAN)).thenReturn(checkingAccount);
    when(accountLoaderServiceMock.loadAccountByIban(RECEIVING_IBAN)).thenReturn(savingsAccount);
    when(transferExecuterMock.appliesTo()).thenReturn(CheckingAccount.class);
    when(transferExecuterMock.transfer(checkingAccount, savingsAccount, AMOUNT)).thenReturn(true);

    underTest.setTransferExecuters(List.of(transferExecuterMock));
    underTest.init();

    underTest.transfer(accountInput);

    verify(accountLoaderServiceMock).loadAccountByIban(SENDING_IBAN);
    verify(accountLoaderServiceMock).loadAccountByIban(RECEIVING_IBAN);
    verify(transferExecuterMock).appliesTo();
    verify(transferExecuterMock).transfer(checkingAccount, savingsAccount, AMOUNT);
    verify(accountRepositoryMock).save(savingsAccount);
    verify(accountRepositoryMock).save(checkingAccount);
  }

  @Test
  public void transferCheckingAccountLocked() {
    AccountTransferInput accountInput = new AccountTransferInput(SENDING_IBAN, RECEIVING_IBAN, AMOUNT);
    CheckingAccount checkingAccount = new CheckingAccount(SENDING_IBAN);
    checkingAccount.setLocked(true);
    when(accountLoaderServiceMock.loadAccountByIban(SENDING_IBAN)).thenReturn(checkingAccount);

    Assertions.assertThrows(AccountLockedException.class, () ->
        underTest.transfer(accountInput));
  }
}