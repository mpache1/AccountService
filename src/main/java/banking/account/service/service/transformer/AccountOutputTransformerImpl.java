package banking.account.service.service.transformer;

import banking.account.service.domain.Account;
import banking.account.service.domain.AccountOutputDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;

@Service
public class AccountOutputTransformerImpl implements AccountOutputTransformer {

  @Override
  public List<AccountOutputDTO> transformAccounts(List<Account> accounts) {
    return accounts.stream().map(this::transformAccount).collect(Collectors.toList());
  }

  @Override
  public AccountOutputDTO transformAccount(Account account) {
    AccountOutputDTO accountOutputDTO = new AccountOutputDTO();
    accountOutputDTO.setIban(account.getIban());
    accountOutputDTO.setLock(account.isLocked() ? "is locked" : "not locked");
    accountOutputDTO.setAccountType(account.getAccountType().toString());
    accountOutputDTO.setAccountHistory(account.getAccountHistory());
    accountOutputDTO.setBalance(formatBalanceForOutput(account.getBalance()));
    accountOutputDTO.setAssociatedIban(account.getAssociatedAccount());
    return accountOutputDTO;
  }
}
