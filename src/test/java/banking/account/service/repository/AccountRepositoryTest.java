package banking.account.service.repository;

import banking.account.service.domain.Account;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.PrivateLoanAccount;
import banking.account.service.domain.SavingsAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AccountRepositoryTest {

  private static final String IBAN = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private AccountRepository accountRepository;

  @Test
  public void findAccountByIban() {
    Account checkingAccount = new CheckingAccount(IBAN);
    entityManager.persist(checkingAccount);
    entityManager.flush();

    Account found = accountRepository.findAccountByIban(checkingAccount.getIban());

    assertEquals(found, checkingAccount);
  }

  @Test
  public void NotFindAccountByIban() {
    Account checkingAccount = new CheckingAccount(IBAN);
    entityManager.persist(checkingAccount);
    entityManager.flush();

    Account found = accountRepository.findAccountByIban(IBAN_2);
    assertNull(found);
  }

  @Test
  public void findAll() {
    Account checkingAccount = new CheckingAccount(IBAN);
    Account privateLoanAccount = new PrivateLoanAccount(IBAN_2);
    entityManager.persist(checkingAccount);
    entityManager.persist(privateLoanAccount);
    entityManager.flush();

    Iterable<Account> allFound = accountRepository.findAll();
    List<Account> accounts = StreamSupport.stream(allFound.spliterator(), false)
        .collect(Collectors.toList());

    assertEquals(accounts.size(), 2);
    assertTrue(accounts.contains(checkingAccount));
    assertTrue(accounts.contains(privateLoanAccount));
  }

  @Test
  public void findAllEmpty() {
    Iterable<Account> allFound = accountRepository.findAll();
    List<Account> accounts = StreamSupport.stream(allFound.spliterator(), false)
        .collect(Collectors.toList());

    assertEquals(accounts.size(), 0);
  }
}