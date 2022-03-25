package banking.account.service.controller;

import banking.account.service.domain.Account;
import banking.account.service.domain.AccountOutputDTO;
import banking.account.service.domain.AccountType;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.input.AccountSavingInput;
import banking.account.service.service.account.AccountService;
import banking.account.service.service.transformer.AccountOutputTransformer;
import banking.account.service.service.transformer.AccountTypesInputTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static java.util.Locale.ENGLISH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

  private MessageSource messageSource;
  private AccountService accountService;
  private AccountOutputTransformer accountOutputTransformer;
  private AccountTypesInputTransformer accountTypesInputTransformer;

  @PostMapping("/create/checking")
  public ResponseEntity<AccountOutputDTO> createCheckingAccount(@RequestBody @Valid AccountInput transaction) {
    Account checkingAccount = accountService.createCheckingAccount(transaction);
    AccountOutputDTO accountOutput = accountOutputTransformer.transformAccount(checkingAccount);
    log.info(messageSource.getMessage("account.created", of(transaction.getIban()).toArray(), ENGLISH));
    return ResponseEntity.status(CREATED).body(accountOutput);
  }

  @PostMapping("/create/saving")
  public ResponseEntity<AccountOutputDTO> createSavingAccount(@RequestBody @Valid AccountSavingInput transaction) {
    Account savingAccount = accountService.createSavingAccount(transaction);
    AccountOutputDTO accountOutput = accountOutputTransformer.transformAccount(savingAccount);
    log.info(messageSource.getMessage("account.created", of(transaction.getIban()).toArray(), ENGLISH));
    return ResponseEntity.status(CREATED).body(accountOutput);
  }

  @PostMapping("/create/privateloan")
  public ResponseEntity<AccountOutputDTO> createPrivateLoanAccount(@RequestBody @Valid AccountInput transaction) {
    Account privateLoadAccount = accountService.createPrivateLoadAccount(transaction);
    AccountOutputDTO accountOutput = accountOutputTransformer.transformAccount(privateLoadAccount);
    log.info(messageSource.getMessage("account.created", of(transaction.getIban()).toArray(), ENGLISH));
    return ResponseEntity.status(CREATED).body(accountOutput);
  }

  @PutMapping("/lock")
  private ResponseEntity<String> lockAccount(@RequestBody @Valid AccountInput transaction) {
    accountService.lockAccount(transaction);
    String message = messageSource.getMessage("account.locked", of(transaction.getIban()).toArray(), ENGLISH);
    log.info(message);
    return ResponseEntity.status(OK).body(message);
  }

  @PutMapping("/unlock")
  private ResponseEntity<String> unlockAccount(@RequestBody @Valid AccountInput transaction) {
    accountService.unlockAccount(transaction);
    String message = messageSource.getMessage("account.unlocked", of(transaction.getIban()).toArray(), ENGLISH);
    log.info(message);
    return ResponseEntity.status(OK).body(message);
  }

  @GetMapping("/{iban}/balance")
  private ResponseEntity<String> getAccountBalance(@PathVariable String iban) {
    Account account = accountService.findAccount(iban);
    return ResponseEntity.status(OK).body(formatBalanceForOutput(account.getBalance()));
  }

  @GetMapping("/{iban}/history")
  private ResponseEntity<List<String>> getAccountHistory(@PathVariable String iban) {
    Account account = accountService.findAccount(iban);
    return ResponseEntity.status(OK).body(account.getAccountHistory());
  }

  @GetMapping("/accounts")
  private ResponseEntity<List<AccountOutputDTO>> getAccounts(@RequestParam(value = "type", required = false) String[] type) {
    Set<AccountType> typeList = type != null ? accountTypesInputTransformer.transformAccountInput(asList(type)) : new HashSet<>();
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

  @Autowired
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }
}