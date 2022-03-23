package banking.account.service.service.deposit;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountDepositInput;
import banking.account.service.domain.input.AccountInput;

public interface DepositService {

  Account deposit(AccountDepositInput transaction);
}
