package banking.account.service.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

import static banking.account.service.domain.AccountType.PRIVATELOAN;

@Entity
@NoArgsConstructor
public class PrivateLoanAccount extends Account {

  public PrivateLoanAccount(String iban) {
    super(iban, PRIVATELOAN);
  }
}
