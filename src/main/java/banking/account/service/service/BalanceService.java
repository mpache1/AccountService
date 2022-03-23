package banking.account.service.service;

import banking.account.service.domain.Account;

import java.math.BigDecimal;

public abstract class BalanceService {

  protected void addAmount(Account account, BigDecimal amount) {
    account.setBalance(account.getBalance().add(amount));
  }

  protected void completeTransfer(Account transferingAccount, Account receivingAccount, BigDecimal amount) {
    withdrawAmount(transferingAccount, amount);
    addAmount(receivingAccount, amount);
  }

  private void withdrawAmount(Account account, BigDecimal amount) {
    account.setBalance(account.getBalance().subtract(amount));
  }
}
