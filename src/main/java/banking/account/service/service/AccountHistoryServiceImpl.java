package banking.account.service.service;

import banking.account.service.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;
import static java.util.Locale.*;

@Service
public class AccountHistoryServiceImpl implements AccountHistoryService {

  private MessageSource messageSource;
  private TimeStampService timeStampService;

  @Override
  public void updateAccountHistoryForDeposit(Account account, BigDecimal amount) {
    String amountOutput = formatBalanceForOutput(amount);
    String timeStamp = timeStampService.generateTimeStamp();
    account.getAccountHistory().add(messageSource.getMessage("account.histoty.deposit", List.of(timeStamp, amountOutput).toArray(), ENGLISH));
  }

  @Override
  public void updateAccountHistorysForTransfer(Account sending, Account receiving, BigDecimal amount) {
    String amountOutput = formatBalanceForOutput(amount);
    String timeStamp = timeStampService.generateTimeStamp();
    sending.getAccountHistory().add(messageSource.getMessage("account.history.transfer.to", List.of(timeStamp, amountOutput, receiving.getIban()).toArray(), ENGLISH));
    receiving.getAccountHistory().add(messageSource.getMessage("account.history.transfer.from", List.of(timeStamp, amountOutput, sending.getIban()).toArray(), ENGLISH));
  }

  @Autowired
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Autowired
  public void setTimeStampService(TimeStampService timeStampService) {
    this.timeStampService = timeStampService;
  }
}
