package banking.account.service.repository;

import banking.account.service.domain.Account;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

  Account findAccountByIban(String iban);

  List<Account> findAll();

  List<Account> findAll(Specification specification);
}
