package banking.account.service.service;

import banking.account.service.domain.AccountOutputDTO;
import banking.account.service.domain.AccountType;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.service.transformer.AccountOutputTransformer;
import banking.account.service.service.transformer.AccountOutputTransformerImpl;
import banking.account.service.util.AccountUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static banking.account.service.domain.AccountType.CHECKING;
import static banking.account.service.util.AccountUtils.formatBalanceForOutput;
import static org.junit.jupiter.api.Assertions.*;

class AccountOutputTransformerTest {

  private static final String IBAN_1 = "DE90123456781234567890";
  private static final BigDecimal AMOUNT = new BigDecimal("200");

  private AccountOutputTransformer underTest;

  @BeforeEach
  public void setUp() {
    underTest = new AccountOutputTransformerImpl();
  }

  @Test
  void transformAccount() {
    CheckingAccount checkingAccount = buildAccount();
    AccountOutputDTO accountOutput = buildAccountOutput();
    AccountOutputDTO result = underTest.transformAccount(checkingAccount);
    assertEquals(result, accountOutput);
  }

  private CheckingAccount buildAccount() {
    CheckingAccount account = new CheckingAccount();
    account.setIban(IBAN_1);
    account.setAccountType(CHECKING);
    account.setAccountHistory(new ArrayList<>());
    account.setLocked(false);
    account.setBalance(AMOUNT);
    return account;
  }

  private AccountOutputDTO buildAccountOutput() {
    AccountOutputDTO account = new AccountOutputDTO();
    account.setIban(IBAN_1);
    account.setAccountType("CHECKING");
    account.setAccountHistory(new ArrayList<>());
    account.setLock("not locked");
    account.setBalance(formatBalanceForOutput(AMOUNT));
    return account;
  }
}