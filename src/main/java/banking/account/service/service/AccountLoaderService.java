package banking.account.service.service;

import banking.account.service.domain.Account;
import banking.account.service.domain.CheckingAccount;

public interface AccountLoaderService {

  Account loadAccountByIban(String iban);

  CheckingAccount loadCheckingAccountByIban(String iban);
}
