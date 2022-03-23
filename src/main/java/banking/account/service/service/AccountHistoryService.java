package banking.account.service.service;

import banking.account.service.domain.Account;

import java.math.BigDecimal;

public interface AccountHistoryService {

  void updateAccountHistoryForDeposit(Account account, BigDecimal amount);

  void updateAccountHistorysForTransfer(Account sending, Account receiving, BigDecimal amount);

}
