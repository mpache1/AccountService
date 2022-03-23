package banking.account.service.service.account;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.AccountType;
import banking.account.service.domain.input.AccountSavingInput;

import java.util.List;

public interface AccountService {

  Account createCheckingAccount(AccountInput transaction);

  Account createSavingAccount(AccountSavingInput transaction);

  Account createPrivateLoadAccount(AccountInput transaction);

  Account findAccount(String iban);

  void lockAccount(AccountInput transaction);

  void unlockAccount(AccountInput transaction);

  List<Account> getAccounts(List<AccountType> accountTypes);
}
