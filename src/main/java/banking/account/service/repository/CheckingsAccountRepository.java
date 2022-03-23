package banking.account.service.repository;

import banking.account.service.domain.CheckingAccount;
import org.springframework.data.repository.CrudRepository;

public interface CheckingsAccountRepository extends CrudRepository<CheckingAccount, Long> {

  CheckingAccount findCheckingAccountByIban(String iban);
}
