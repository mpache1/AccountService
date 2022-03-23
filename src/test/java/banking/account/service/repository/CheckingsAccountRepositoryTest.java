package banking.account.service.repository;

import banking.account.service.domain.Account;
import banking.account.service.domain.CheckingAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CheckingsAccountRepositoryTest {

  private static final String IBAN = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private CheckingsAccountRepository checkingsAccountRepository;

  @Test
  public void findCheckingAccountByIban() {
    Account checkingAccount = new CheckingAccount(IBAN);
    entityManager.persist(checkingAccount);
    entityManager.flush();

    Account found = checkingsAccountRepository.findCheckingAccountByIban(checkingAccount.getIban());

    assertEquals(found, checkingAccount);
  }

  @Test
  public void NotFindCheckingAccountByIban() {
    Account checkingAccount = new CheckingAccount(IBAN);
    entityManager.persist(checkingAccount);
    entityManager.flush();

    Account found = checkingsAccountRepository.findCheckingAccountByIban(IBAN_2);
    assertNull(found);
  }

}