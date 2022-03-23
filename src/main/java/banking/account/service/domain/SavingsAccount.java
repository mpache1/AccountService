package banking.account.service.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

import static banking.account.service.domain.AccountType.SAVING;

@Entity
@NoArgsConstructor
public class SavingsAccount extends Account {

  public SavingsAccount(String iban, String associatedAccount) {
    super(iban, SAVING);
    this.setAssociatedAccount(associatedAccount);
  }
}
