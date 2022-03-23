package banking.account.service.integration;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.AccountOutputDTO;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.input.AccountSavingInput;
import banking.account.service.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static banking.account.service.domain.AccountType.CHECKING;
import static banking.account.service.domain.AccountType.PRIVATELOAN;
import static banking.account.service.domain.AccountType.SAVING;
import static banking.account.service.util.AccountUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIntegrationTest {

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
  public void createCheckingAccount() {
    AccountInput accountInput = new AccountInput(IBAN_1);

    ResponseEntity<AccountOutputDTO> responseEntity =
        this.restTemplate.postForEntity("http://localhost:" + port + "/v1/create/checking", accountInput, AccountOutputDTO.class);

    Account accountResult = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountResult.getIban(), IBAN_1);
    assertEquals(accountResult.getBalance(), ZERO);
    assertEquals(accountResult.getAccountType(), CHECKING);

    assertNotNull(responseEntity.getBody());
    assertEquals(responseEntity.getBody().getIban(), IBAN_1);
  }

  @Test
  public void createSavingAccount() {
    AccountSavingInput accountInput = new AccountSavingInput(IBAN_1, IBAN_2);
    accountRepository.save(new CheckingAccount(IBAN_2));

    ResponseEntity<AccountOutputDTO> responseEntity =
        this.restTemplate.postForEntity("http://localhost:" + port + "/v1/create/saving", accountInput, AccountOutputDTO.class);

    Account accountResult = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountResult.getIban(), IBAN_1);
    assertEquals(accountResult.getBalance(), ZERO);
    assertEquals(accountResult.getAccountType(), SAVING);
    assertEquals(accountResult.getAssociatedAccount(), IBAN_2);

    assertNotNull(responseEntity.getBody());
    assertEquals(responseEntity.getBody().getIban(), IBAN_1);
  }

  @Test
  public void createPrivateLoanAccount() {
    AccountInput accountInput = new AccountInput(IBAN_1);

    ResponseEntity<AccountOutputDTO> responseEntity =
        this.restTemplate.postForEntity("http://localhost:" + port + "/v1/create/privateloan", accountInput, AccountOutputDTO.class);

    Account accountResult = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountResult.getIban(), IBAN_1);
    assertEquals(accountResult.getBalance(), ZERO);
    assertEquals(accountResult.getAccountType(), PRIVATELOAN);

    assertNotNull(responseEntity.getBody());
    assertEquals(responseEntity.getBody().getIban(), IBAN_1);
  }

  @Test
  public void createIbanAlreadyAssigned() {
    AccountInput accountInput = new AccountInput(IBAN_1);
    accountRepository.save(new CheckingAccount(IBAN_1));

    ResponseEntity<String> responseEntity =
        this.restTemplate.postForEntity("http://localhost:" + port + "/v1/create/privateloan", accountInput, String.class);

    assertEquals(responseEntity.getStatusCode(), BAD_REQUEST);
  }

  @Test
  public void lockAccount() {
    AccountInput accountInput = new AccountInput(IBAN_1);
    accountRepository.save(new CheckingAccount(IBAN_1));

    this.restTemplate.put("http://localhost:" + port + "/v1/lock", accountInput, String.class);

    Account accountResult = accountRepository.findAccountByIban(IBAN_1);
    assertTrue(accountResult.isLocked());
  }

  @Test
  public void unlockAccount() {
    AccountInput accountInput = new AccountInput(IBAN_1);
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    checkingAccount.setLocked(true);
    accountRepository.save(checkingAccount);

    this.restTemplate.put("http://localhost:" + port + "/v1/unlock", accountInput, String.class);

    Account accountResult = accountRepository.findAccountByIban(IBAN_1);
    assertFalse(accountResult.isLocked());
  }

  @Test
  public void accountBalance() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    checkingAccount.setBalance(AMOUNT);
    accountRepository.save(checkingAccount);

    ResponseEntity<String> response = this.restTemplate.getForEntity("http://localhost:" + port + "/v1/" + IBAN_1 + "/balance", String.class);

    assertEquals(response.getBody(), formatBalanceForOutput(AMOUNT));
  }

  @Test
  public void accountHistory() {
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    checkingAccount.getAccountHistory().add("+100.00 has been deposited");
    accountRepository.save(checkingAccount);

    ResponseEntity<String> response = this.restTemplate.getForEntity("http://localhost:" + port + "/v1/" + IBAN_1 + "/history", String.class);

    assertEquals(response.getBody(), "[\"+100.00 has been deposited\"]");
  }
}
