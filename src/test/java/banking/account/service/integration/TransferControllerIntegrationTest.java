package banking.account.service.integration;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.PrivateLoanAccount;
import banking.account.service.domain.SavingsAccount;
import banking.account.service.domain.input.AccountTransferInput;
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
public class TransferControllerIntegrationTest {

  private static final String IBAN_1 = "DE90123456781234567890";
  private static final String IBAN_2 = "DE90123456781234567891";
  private static final String IBAN_3 = "DE90123456781234567891";
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
  public void transferFromChecking() {
    AccountTransferInput accountInput = new AccountTransferInput(IBAN_1, IBAN_2, AMOUNT);
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN_2, IBAN_1);
    accountRepository.save(checkingAccount);
    accountRepository.save(savingsAccount);

    this.restTemplate.put("http://localhost:" + port + "/v1/transfer", accountInput, String.class);

    Account accountReceiving = accountRepository.findAccountByIban(IBAN_2);
    assertEquals(accountReceiving.getIban(), savingsAccount.getIban());
    assertEquals(accountReceiving.getBalance(), AMOUNT);
    assertEquals(accountReceiving.getAccountHistory().size(), 1);

    Account accountSending = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountSending.getIban(), checkingAccount.getIban());
    assertEquals(accountSending.getBalance(), AMOUNT.negate());
    assertEquals(accountSending.getAccountHistory().size(), 1);
  }

  @Test
  public void transferCheckingAccountNotFound() {
    AccountTransferInput accountInput = new AccountTransferInput(IBAN_1, IBAN_2, AMOUNT);
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    accountRepository.save(checkingAccount);

    this.restTemplate.put("http://localhost:" + port + "/v1/transfer", accountInput, String.class);

    Account accountSending = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountSending.getIban(), checkingAccount.getIban());
    assertEquals(accountSending.getBalance(), ZERO);
    assertEquals(accountSending.getAccountHistory().size(), 0);
  }

  @Test
  public void transferFromSavingToAssociated() {
    AccountTransferInput accountInput = new AccountTransferInput(IBAN_2, IBAN_1, AMOUNT);
    CheckingAccount checkingAccount = new CheckingAccount(IBAN_1);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN_2, IBAN_1);
    accountRepository.save(checkingAccount);
    accountRepository.save(savingsAccount);

    this.restTemplate.put("http://localhost:" + port + "/v1/transfer", accountInput, String.class);

    Account accountReceiving = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountReceiving.getIban(), checkingAccount.getIban());
    assertEquals(accountReceiving.getBalance(), AMOUNT);
    assertEquals(accountReceiving.getAccountHistory().size(), 1);

    Account accountSending = accountRepository.findAccountByIban(IBAN_2);
    assertEquals(accountSending.getIban(), savingsAccount.getIban());
    assertEquals(accountSending.getBalance(), AMOUNT.negate());
    assertEquals(accountSending.getAccountHistory().size(), 1);
  }

  @Test
  public void transferFromSavingToNotAssociated() {
    AccountTransferInput accountInput = new AccountTransferInput(IBAN_2, IBAN_3, AMOUNT);
    CheckingAccount checkingAccount1 = new CheckingAccount(IBAN_1);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN_2, IBAN_1);
    accountRepository.save(checkingAccount1);
    accountRepository.save(savingsAccount);

    this.restTemplate.put("http://localhost:" + port + "/v1/transfer", accountInput, String.class);

    Account accountReceiving = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountReceiving.getBalance(), ZERO);
    assertEquals(accountReceiving.getAccountHistory().size(), 0);

    Account accountSending = accountRepository.findAccountByIban(IBAN_2);
    assertEquals(accountSending.getBalance(), ZERO);
    assertEquals(accountSending.getAccountHistory().size(), 0);
  }

  @Test
  public void transferFromPrivatLoan() {
    AccountTransferInput accountInput = new AccountTransferInput(IBAN_1, IBAN_2, AMOUNT);
    PrivateLoanAccount privateLoanAccount = new PrivateLoanAccount(IBAN_1);
    CheckingAccount checkingAccount  = new CheckingAccount(IBAN_2);
    accountRepository.save(privateLoanAccount);
    accountRepository.save(checkingAccount);

    this.restTemplate.put("http://localhost:" + port + "/v1/transfer", accountInput, String.class);

    Account accountReceiving = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountReceiving.getBalance(), ZERO);
    assertEquals(accountReceiving.getAccountHistory().size(), 0);
  }

  @Test
  public void transferAccountLocked() {
    AccountTransferInput accountInput = new AccountTransferInput(IBAN_1, IBAN_2, AMOUNT);
    CheckingAccount checkingAccount1 = new CheckingAccount(IBAN_1);
    checkingAccount1.setLocked(true);
    CheckingAccount checkingAccount2  = new CheckingAccount(IBAN_2);
    accountRepository.save(checkingAccount1);
    accountRepository.save(checkingAccount2);

    this.restTemplate.put("http://localhost:" + port + "/v1/transfer", accountInput, String.class);

    Account accountReceiving = accountRepository.findAccountByIban(IBAN_1);
    assertEquals(accountReceiving.getBalance(), ZERO);
    assertEquals(accountReceiving.getAccountHistory().size(), 0);
  }
}
