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
import org.springframework.stereotype.Service;

import java.util.List;
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
  public List<Account> getAccounts(List<AccountType> accountTypes) {
    Iterable<Account> loadedAccounts = accountRepository.findAll();
    List<Account> result = StreamSupport.stream(loadedAccounts.spliterator(), false)
        .collect(Collectors.toList());
    if (!accountTypes.isEmpty()) {
      result = filterAccountsByTypes(result, accountTypes);
    }
    return result;
  }

  private List<Account> filterAccountsByTypes(List<Account> accounts, List<AccountType> accountTypes) {
    return accounts.stream()
        .filter(account -> accountTypes.contains(account.getAccountType()))
        .collect(Collectors.toList());
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
