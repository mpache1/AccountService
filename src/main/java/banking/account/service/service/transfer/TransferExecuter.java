package banking.account.service.service.transfer;

import banking.account.service.domain.Account;

import java.math.BigDecimal;

public interface TransferExecuter{

  Class<? extends Account> appliesTo();

  boolean transfer(Account sending, Account recieving, BigDecimal amount);
}
