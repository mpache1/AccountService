package banking.account.service.service.transformer;

import banking.account.service.domain.AccountType;

import java.util.List;
import java.util.Set;

public interface AccountTypesInputTransformer {

  Set<AccountType> transformAccountInput(List<String> accontTypeStrings);

}
