package banking.account.service.integration;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountDepositInput;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DepositControllerIntegrationTest {

  private static final String IBAN_1 = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";
  private static final BigDecimal AMOUNT = new BigDecimal("100.00");
  private static final BigDecimal ZERO = new BigDecimal("0.00");

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @Autowired
  private AccountRepository accountRepository;

  @BeforeEach
  public void setUp() {
    accountRepository.deleteAll();
  }

  @Test
  public void deposit() {
    AccountDepositInput accountInput = new AccountDepositInput(IBAN_1, AMOUNT);
    CheckingAccount account = new CheckingAccount(IBAN_1);
    accountRepository.save(account);

    this.restTemplate.put("http://localhost:" + port + "/v1/deposit", accountInput, String.class);

    Account accountResult = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountResult.getIban(), account.getIban());
    assertEquals(accountResult.getBalance(), AMOUNT);
    assertEquals(accountResult.getAccountHistory().size(), 1);
  }

  @Test
  public void depositAccountNotFound() {
    AccountDepositInput accountInput = new AccountDepositInput(IBAN_1, AMOUNT);
    CheckingAccount account = new CheckingAccount(IBAN_2);
    accountRepository.save(account);

    this.restTemplate.put("http://localhost:" + port + "/v1/deposit", accountInput, String.class);

    Account accountResult = accountRepository.findAccountByIban(IBAN_2);
    assertEquals(accountResult.getBalance(), ZERO);
    assertEquals(accountResult.getAccountHistory().size(), 0);
  }

  @Test
  public void depositAccountLocked() {
    AccountDepositInput accountInput = new AccountDepositInput(IBAN_1, AMOUNT);
    CheckingAccount account = new CheckingAccount(IBAN_1);
    account.setLocked(true);
    accountRepository.save(account);

    this.restTemplate.put("http://localhost:" + port + "/v1/deposit", accountInput, String.class);

    Account accountResult = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountResult.getIban(), account.getIban());
    assertEquals(accountResult.getBalance(), ZERO);
    assertEquals(accountResult.getAccountHistory().size(), 0);
  }
}
