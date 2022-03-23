package banking.account.service.repository;

import banking.account.service.domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

  Account findAccountByIban(String iban);

  Iterable<Account> findAll();
}
