package banking.account.service.service;

import banking.account.service.domain.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;

@Service
public class AccountHistoryServiceImpl implements AccountHistoryService {

  @Override
  public void updateAccountHistoryForDeposit(Account account, BigDecimal amount) {
    String amountOutput = formatBalanceForOutput(amount);
    account.getAccountHistory().add("+" + amountOutput + " has been deposited");
  }

  @Override
  public void updateAccountHistorysForTransfer(Account sending, Account receiving, BigDecimal amount) {
    String amountOutput = formatBalanceForOutput(amount);
    sending.getAccountHistory().add("-" + amountOutput + " transfered to " + receiving.getIban());
    receiving.getAccountHistory().add("+" + amountOutput + " transfered from " + sending.getIban());
  }
}
