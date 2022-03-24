package banking.account.service.service.transformer;

import banking.account.service.domain.AccountType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static banking.account.service.domain.AccountType.CHECKING;
import static banking.account.service.domain.AccountType.PRIVATELOAN;
import static banking.account.service.domain.AccountType.SAVING;

@Service
public class AccountTypesInputTransformerImpl implements AccountTypesInputTransformer {

  private static final Map<String, AccountType> ACCOUNT_TYP_MAP = buildAccountMapping();

  private static HashMap<String, AccountType> buildAccountMapping() {
    HashMap<String, AccountType> accountMap = new HashMap<>();
    accountMap.put("checking", CHECKING);
    accountMap.put("saving", SAVING);
    accountMap.put("privateloan", PRIVATELOAN);
    return accountMap;
  }

  @Override
  public Set<AccountType> transformAccountInput(List<String> accontTypeStrings) {
    return accontTypeStrings.stream()
        .filter(ACCOUNT_TYP_MAP::containsKey)
        .map(ACCOUNT_TYP_MAP::get)
        .collect(Collectors.toSet());
  }
}
