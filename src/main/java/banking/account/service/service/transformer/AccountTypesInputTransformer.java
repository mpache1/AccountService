package banking.account.service.service.transformer;

import banking.account.service.domain.AccountType;

import java.util.List;

public interface AccountTypesInputTransformer {

  List<AccountType> transformAccountInput(List<String> accontTypeStrings);

}
