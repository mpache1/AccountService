package banking.account.service.service.transformer;

import banking.account.service.domain.Account;
import banking.account.service.domain.AccountOutputDTO;

import java.util.List;

public interface AccountOutputTransformer {

  List<AccountOutputDTO> transformAccounts(List<Account> accounts);

  AccountOutputDTO transformAccount(Account account);
}
