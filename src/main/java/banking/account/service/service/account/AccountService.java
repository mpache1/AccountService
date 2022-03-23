package banking.account.service.service.account;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.AccountType;
import banking.account.service.domain.input.AccountSavingInput;

import java.util.List;

public interface AccountService {

  Account createCheckingAccount(AccountInput accountInput);

  Account createSavingAccount(AccountSavingInput accountInput);

  Account createPrivateLoadAccount(AccountInput accountInput);

  Account findAccount(String iban);

  void lockAccount(AccountInput accountInput);

  void unlockAccount(AccountInput accountInput);

  List<Account> getAccounts(List<AccountType> accountTypes);
}
