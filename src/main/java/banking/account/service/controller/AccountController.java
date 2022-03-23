package banking.account.service.controller;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.AccountOutputDTO;
import banking.account.service.domain.AccountType;
import banking.account.service.domain.input.AccountSavingInput;
import banking.account.service.service.account.AccountService;
import banking.account.service.service.transformer.AccountOutputTransformer;
import banking.account.service.service.transformer.AccountTypesInputTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

  private AccountService accountService;
  private AccountOutputTransformer accountOutputTransformer;
  private AccountTypesInputTransformer accountTypesInputTransformer;

  @PostMapping("/create/checking")
  public ResponseEntity<AccountOutputDTO> createCheckingAccount(@RequestBody @Valid AccountInput accountInput) {
    Account checkingAccount = accountService.createCheckingAccount(accountInput);
    AccountOutputDTO accountOutput = accountOutputTransformer.transformAccount(checkingAccount);
    log.info(accountInput.getIban() + " created");
    return ResponseEntity.status(CREATED).body(accountOutput);
  }

  @PostMapping("/create/saving")
  public ResponseEntity<AccountOutputDTO> createSavingAccount(@RequestBody @Valid AccountSavingInput accountInput) {
    Account savingAccount = accountService.createSavingAccount(accountInput);
    AccountOutputDTO accountOutput = accountOutputTransformer.transformAccount(savingAccount);
    log.info(accountInput.getIban() + " created");
    return ResponseEntity.status(CREATED).body(accountOutput);
  }

  @PostMapping("/create/privateloan")
  public ResponseEntity<AccountOutputDTO> createPrivateLoanAccount(@RequestBody @Valid AccountInput accountInput) {
    Account privateLoadAccount = accountService.createPrivateLoadAccount(accountInput);
    AccountOutputDTO accountOutput = accountOutputTransformer.transformAccount(privateLoadAccount);
    log.info(accountInput.getIban() + " created");
    return ResponseEntity.status(CREATED).body(accountOutput);
  }

  @PutMapping("/lock")
  private ResponseEntity<String> lockAccount(@RequestBody @Valid AccountInput accountInput) {
    accountService.lockAccount(accountInput);
    log.info(accountInput.getIban() + " was locked");
    return ResponseEntity.status(OK).body("Account: " + accountInput.getIban() + " has been locked");
  }

  @PutMapping("/unlock")
  private ResponseEntity<String> unlockAccount(@RequestBody @Valid AccountInput accountInput) {
    accountService.unlockAccount(accountInput);
    log.info(accountInput.getIban() + " was unlocked");
    return ResponseEntity.status(OK).body("Account: " + accountInput.getIban() + " has been unlocked");
  }

  @GetMapping("/{iban}/balance")
  private ResponseEntity<String> getAccountBalance(@PathVariable() @Size(min = 22, max = 22) String iban) {
    Account account = accountService.findAccount(iban);
    return ResponseEntity.status(OK).body(formatBalanceForOutput(account.getBalance()));
  }

  @Validated
  @GetMapping("/{iban}/history")
  private ResponseEntity<List<String>> getAccountHistory(@PathVariable() @Size(min = 22, max = 22) String iban) {
    Account account = accountService.findAccount(iban);
    return ResponseEntity.status(OK).body(account.getAccountHistory());
  }

  @GetMapping("/accounts")
  private ResponseEntity<List<AccountOutputDTO>> getAccounts(@RequestParam(value = "type", required = false) String[] type) {
    List<AccountType> typeList = type != null ? accountTypesInputTransformer.transformAccountInput(asList(type)) : new ArrayList<>();
    List<Account> accounts = accountService.getAccounts(typeList);
    List<AccountOutputDTO> accountOutputs = accountOutputTransformer.transformAccounts(accounts);
    return ResponseEntity.status(OK).body(accountOutputs);
  }

  @Autowired
  public void setAccountTypesInputTransformer(AccountTypesInputTransformer accountTypesInputTransformer) {
    this.accountTypesInputTransformer = accountTypesInputTransformer;
  }

  @Autowired
  public void setAccountOutputTransformer(AccountOutputTransformer accountOutputTransformer) {
    this.accountOutputTransformer = accountOutputTransformer;
  }

  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }
}