package banking.account.service.service;

import banking.account.service.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;
import static java.util.Locale.*;

@Service
public class AccountHistoryServiceImpl implements AccountHistoryService {

  private MessageSource messageSource;

  @Override
  public void updateAccountHistoryForDeposit(Account account, BigDecimal amount) {
    String amountOutput = formatBalanceForOutput(amount);
    account.getAccountHistory().add(messageSource.getMessage("account.histoty.deposit", List.of(amountOutput).toArray(), ENGLISH));
  }

  @Override
  public void updateAccountHistorysForTransfer(Account sending, Account receiving, BigDecimal amount) {
    String amountOutput = formatBalanceForOutput(amount);
    sending.getAccountHistory().add(messageSource.getMessage("account.history.transfer.to", List.of(amountOutput, receiving.getIban()).toArray(), ENGLISH));
    receiving.getAccountHistory().add(messageSource.getMessage("account.history.transfer.from", List.of(amountOutput, sending.getIban()).toArray(), ENGLISH));
  }

  @Autowired
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }
}
