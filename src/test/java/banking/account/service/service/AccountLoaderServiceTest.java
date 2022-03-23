package banking.account.service.service;

import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.PrivateLoanAccount;
import banking.account.service.exception.AccountNotFoundException;
import banking.account.service.repository.AccountRepository;
import banking.account.service.repository.CheckingsAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountLoaderServiceTest {

  private static final String IBAN_1 = "DE90123456781234567890";

  @InjectMocks
  AccountLoaderServiceImpl underTest;
  @Mock
  private AccountRepository accountRepositoryMock;
  @Mock
  private CheckingsAccountRepository checkingsAccountRepositoryMock;

  @BeforeEach
  public void setUp() {
    openMocks(this);
  }

  @Test
  void loadAccountByIban() {
    PrivateLoanAccount privateLoanAccount = new PrivateLoanAccount(IBAN_1);
    when(accountRepositoryMock.findAccountByIban(IBAN_1)).thenReturn(privateLoanAccount);

    underTest.loadAccountByIban(IBAN_1);

    verify(accountRepositoryMock).findAccountByIban(IBAN_1);
  }

  @Test
  void loadCheckingAccountByIban() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    when(checkingsAccountRepositoryMock.findCheckingAccountByIban(IBAN_1)).thenReturn(checkingAccount);

    underTest.loadCheckingAccountByIban(IBAN_1);

    verify(checkingsAccountRepositoryMock).findCheckingAccountByIban(IBAN_1);
  }

  @Test
  void loadAccountByIbanException() {
    when(accountRepositoryMock.findAccountByIban(IBAN_1)).thenReturn(null);
    Assertions.assertThrows(AccountNotFoundException.class, () ->
        underTest.loadCheckingAccountByIban(IBAN_1));
  }

  @Test
  void loadCheckingAccountByIbanException() {
    when(checkingsAccountRepositoryMock.findCheckingAccountByIban(IBAN_1)).thenReturn(null);
    Assertions.assertThrows(AccountNotFoundException.class, () ->
        underTest.loadCheckingAccountByIban(IBAN_1));
  }
}