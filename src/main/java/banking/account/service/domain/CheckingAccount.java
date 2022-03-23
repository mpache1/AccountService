package banking.account.service.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

import static banking.account.service.domain.AccountType.CHECKING;

@Entity
@NoArgsConstructor
public class CheckingAccount extends Account {

  public CheckingAccount(String iban) {
    super(iban, CHECKING);
  }
}
