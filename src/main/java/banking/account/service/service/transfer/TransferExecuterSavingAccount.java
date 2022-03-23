package banking.account.service.service.transfer;

import banking.account.service.domain.Account;
import banking.account.service.domain.SavingsAccount;
import banking.account.service.service.AccountHistoryService;
import banking.account.service.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferExecuterSavingAccount extends BalanceService implements TransferExecuter {

  private AccountHistoryService accountHistoryService;

  @Override
  public Class<SavingsAccount> appliesTo() {
    return SavingsAccount.class;
  }

  @Override
  public boolean transfer(Account sending, Account receiving, BigDecimal amount) {
    if (sending.getAssociatedAccount().equals(receiving.getIban())) {
      completeTransfer(sending, receiving, amount);
      accountHistoryService.updateAccountHistorysForTransfer(sending, receiving, amount);
      return true;
    }
    return false;
  }

  @Autowired
  public void setAccountHistoryService(AccountHistoryService accountHistoryService) {
    this.accountHistoryService = accountHistoryService;
  }
}
