package banking.account.service.service;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.PrivateLoanAccount;
import banking.account.service.domain.SavingsAccount;
import banking.account.service.domain.input.AccountSavingInput;
import banking.account.service.repository.AccountRepository;
import banking.account.service.service.account.AccountServiceImpl;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static banking.account.service.domain.AccountType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class AccountServiceTest {

  private static final String IBAN_1 = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";
  private static final String IBAN_3 = "DE90123456781234567892";

  @InjectMocks
  private AccountServiceImpl underTest;
  @Mock
  private AccountRepository accountRepositoryMock;
  @Mock
  private AccountLoaderService accountLoaderServiceMock;

  @BeforeEach
  public void SetUp() {
    openMocks(this);
  }

  @Test
  public void createCheckingAccount() {
    AccountInput accountInput = new AccountInput(IBAN_1);
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    when(accountRepositoryMock.save(any())).thenReturn(checkingAccount);

    Account result = underTest.createCheckingAccount(accountInput);

    verify(accountRepositoryMock).save(checkingAccount);

    assertNotNull(result);
    assertEquals(result.getIban(), IBAN_1);
    assertEquals(result, checkingAccount);
  }

  @Test
  public void createSavingsAccount() {
    AccountSavingInput accountInput = new AccountSavingInput(IBAN_1, IBAN_2);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN_1, IBAN_2);
    when(accountLoaderServiceMock.loadCheckingAccountByIban(IBAN_2)).thenReturn(new CheckingAccount(IBAN_2));
    when(accountRepositoryMock.save(any())).thenReturn(savingsAccount);

    Account result = underTest.createSavingAccount(accountInput);

    verify(accountLoaderServiceMock).loadCheckingAccountByIban(IBAN_2);
    verify(accountRepositoryMock).save(savingsAccount);

    assertNotNull(result);
    assertEquals(result.getIban(), IBAN_1);
    assertEquals(result.getAssociatedAccount(), IBAN_2);
    assertEquals(result, savingsAccount);
  }

  @Test
  public void createPrivateLoanAccount() {
    AccountInput accountInput = new AccountInput(IBAN_1);
    PrivateLoanAccount privateLoanAccount = new PrivateLoanAccount(IBAN_1);
    when(accountRepositoryMock.save(any())).thenReturn(privateLoanAccount);

    Account result = underTest.createPrivateLoadAccount(accountInput);

    verify(accountRepositoryMock).save(privateLoanAccount);

    assertNotNull(result);
    assertEquals(result.getIban(), IBAN_1);
    assertEquals(result, privateLoanAccount);
  }

  @Test
  public void lockAccount() {
    AccountInput accountInput = new AccountInput(IBAN_1);
    PrivateLoanAccount account = new PrivateLoanAccount(IBAN_1);
    when(accountRepositoryMock.save(any())).thenReturn(account);
    when(accountLoaderServiceMock.loadAccountByIban(IBAN_1)).thenReturn(account);

    underTest.lockAccount(accountInput);

    verify(accountLoaderServiceMock).loadAccountByIban(IBAN_1);
    verify(accountRepositoryMock).save(account);

    assertTrue(account.isLocked());
  }

  @Test
  public void unlockAccount() {
    AccountInput accountInput = new AccountInput(IBAN_1);
    PrivateLoanAccount account = new PrivateLoanAccount(IBAN_1);
    when(accountRepositoryMock.save(any())).thenReturn(account);
    when(accountLoaderServiceMock.loadAccountByIban(IBAN_1)).thenReturn(account);

    underTest.unlockAccount(accountInput);

    verify(accountLoaderServiceMock).loadAccountByIban(IBAN_1);
    verify(accountRepositoryMock).save(account);

    assertFalse(account.isLocked());
  }

  @Test
  public void findAccount() {
    PrivateLoanAccount account = new PrivateLoanAccount(IBAN_1);
    when(accountLoaderServiceMock.loadAccountByIban(IBAN_1)).thenReturn(account);

    underTest.findAccount(IBAN_1);

    verify(accountLoaderServiceMock).loadAccountByIban(IBAN_1);

    assertEquals(account.getIban(), IBAN_1);
  }

  @Test
  public void getAccount() {
    PrivateLoanAccount privateLoanAccount = new PrivateLoanAccount(IBAN_1);
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_2);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN_3, IBAN_2);
    List<Account> loadedAccounts = List.of(privateLoanAccount, checkingAccount, savingsAccount);
    when(accountRepositoryMock.findAll()).thenReturn(loadedAccounts);

    List<Account> loadedAccountList = underTest.getAccounts(new HashSet<>());

    verify(accountRepositoryMock).findAll();

    assertEquals(loadedAccountList.size(), 3);
    assertTrue(loadedAccountList.contains(savingsAccount));
    assertTrue(loadedAccountList.contains(checkingAccount));
    assertTrue(loadedAccountList.contains(privateLoanAccount));
  }

  @Test
  public void getAccountWithFilter() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_2);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN_3, IBAN_2);
    List<Account> loadedAccounts = List.of(checkingAccount, savingsAccount);

    ArgumentCaptor<Specification<Account>> specificationsCaptor = ArgumentCaptor.forClass(Specification.class);
    BDDMockito.given(accountRepositoryMock.findAll(specificationsCaptor.capture())).willReturn(loadedAccounts);

    List<Account> loadedAccountList = underTest.getAccounts(Set.of(CHECKING, SAVING));

    verify(accountRepositoryMock).findAll(specificationsCaptor.capture());

    assertEquals(loadedAccountList.size(), 2);
    assertTrue(loadedAccountList.contains(savingsAccount));
    assertTrue(loadedAccountList.contains(checkingAccount));
  }
}