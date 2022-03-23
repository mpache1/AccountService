package banking.account.service.service;

import antlr.StringUtils;
import banking.account.service.domain.Account;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.exception.AccountNotFoundException;
import banking.account.service.repository.AccountRepository;
import banking.account.service.repository.CheckingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountLoaderServiceImpl implements AccountLoaderService {

  private AccountRepository accountRepository;
  private CheckingsAccountRepository checkingsAccountRepository;

  @Override
  public Account loadAccountByIban(String iban) {
    iban = iban.toUpperCase();
    Optional<Account> loadedAccount = Optional.ofNullable(accountRepository.findAccountByIban(iban));
    if (loadedAccount.isPresent()) {
      return loadedAccount.get();
    }
    throw new AccountNotFoundException(iban);
  }

  @Override
  public CheckingAccount loadCheckingAccountByIban(String iban) {
    iban = iban.toUpperCase();
    Optional<CheckingAccount> loadedAccount = Optional.ofNullable(checkingsAccountRepository.findCheckingAccountByIban(iban));
    if (loadedAccount.isPresent()) {
      return loadedAccount.get();
    }
    throw new AccountNotFoundException(iban);
  }

  @Autowired
  public void setCheckingsAccountRepository(CheckingsAccountRepository checkingsAccountRepository) {
    this.checkingsAccountRepository = checkingsAccountRepository;
  }

  @Autowired
  public void setAccountRepository(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
}
