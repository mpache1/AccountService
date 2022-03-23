package banking.account.service.service.transfer;

import banking.account.service.domain.Account;
import banking.account.service.domain.PrivateLoanAccount;
import banking.account.service.service.BalanceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferExecuterPrivatLoanAccount extends BalanceService implements TransferExecuter {

  @Override
  public Class<PrivateLoanAccount> appliesTo() {
    return PrivateLoanAccount.class;
  }

  @Override
  public boolean transfer(Account sending, Account recieving, BigDecimal amount) {
    return false;
  }
}
