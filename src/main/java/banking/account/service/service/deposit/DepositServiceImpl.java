package banking.account.service.service.deposit;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountDepositInput;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.exception.AccountLockedException;
import banking.account.service.repository.AccountRepository;
import banking.account.service.service.AccountHistoryService;
import banking.account.service.service.AccountLoaderService;
import banking.account.service.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositServiceImpl extends BalanceService implements DepositService {

  private AccountHistoryService accountHistoryService;
  private AccountRepository accountRepository;
  private AccountLoaderService accountLoaderService;

  @Override
  public Account deposit(AccountDepositInput transaction) {
    Account account = accountLoaderService.loadAccountByIban(transaction.getIban());
    if (account.isLocked()) {
      throw new AccountLockedException(transaction.getIban());
    }
    BigDecimal amount = transaction.getAmount();
    addAmount(account, amount);
    accountHistoryService.updateAccountHistoryForDeposit(account, amount);
    return accountRepository.save(account);
  }

  @Autowired
  public void setAccountLoaderService(AccountLoaderService accountLoaderService) {
    this.accountLoaderService = accountLoaderService;
  }

  @Autowired
  public void setAccountHistoryService(AccountHistoryService accountHistoryService) {
    this.accountHistoryService = accountHistoryService;
  }

  @Autowired
  public void setAccountRepository(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
}
