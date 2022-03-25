package banking.account.service.service.account;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.AccountType;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.PrivateLoanAccount;
import banking.account.service.domain.SavingsAccount;
import banking.account.service.domain.input.AccountSavingInput;
import banking.account.service.repository.AccountRepository;
import banking.account.service.service.AccountLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AccountServiceImpl implements AccountService {

  private AccountRepository accountRepository;
  private AccountLoaderService accountLoaderService;

  @Override
  public Account createCheckingAccount(AccountInput transaction) {
    return accountRepository.save(new CheckingAccount(transaction.getIban()));
  }

  @Override
  public Account createPrivateLoadAccount(AccountInput transaction) {
    return accountRepository.save(new PrivateLoanAccount(transaction.getIban()));
  }

  @Override
  public Account createSavingAccount(AccountSavingInput transaction) {
    CheckingAccount checkingAccount = accountLoaderService.loadCheckingAccountByIban(transaction.getAssociatedIban());
    return accountRepository.save(new SavingsAccount(transaction.getIban(), checkingAccount.getIban()));
  }

  @Override
  public void lockAccount(AccountInput transaction) {
    lockAccount(transaction.getIban(), true);
  }

  @Override
  public void unlockAccount(AccountInput transaction) {
    lockAccount(transaction.getIban(), false);
  }

  @Override
  public Account findAccount(String iban) {
    return accountLoaderService.loadAccountByIban(iban);
  }

  @Override
  public List<Account> getAccounts(Set<AccountType> accountTypes) {
    List<Account> loadedAccounts;
    if (accountTypes.isEmpty()) {
      loadedAccounts = accountRepository.findAll();
    }
    else {
      loadedAccounts = accountRepository.findAll(accountTypeContain(accountTypes));
    }
    return loadedAccounts;
  }

  public static Specification<Account> accountTypeContain(Set<AccountType> accountTypes) {
    return (account, cq, cb) -> account.get("accountType").in(accountTypes);
  }

  private void lockAccount(String iban, boolean lock) {
    Account loadedAccount = accountLoaderService.loadAccountByIban(iban);
    loadedAccount.setLocked(lock);
    accountRepository.save(loadedAccount);
  }

  @Autowired
  public void setAccountRepository(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Autowired
  public void setAccountLoaderService(AccountLoaderService accountLoaderService) {
    this.accountLoaderService = accountLoaderService;
  }
}
