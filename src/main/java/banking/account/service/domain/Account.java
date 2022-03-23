package banking.account.service.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {

  @Id
  @Column(unique = true)
  String iban;
  @ElementCollection(fetch = EAGER)
  List<String> accountHistory;
  AccountType accountType;
  BigDecimal balance;
  boolean locked;
  String associatedAccount;

  public Account(String iban, AccountType accountType) {
    this.iban = iban.toUpperCase();
    this.accountType = accountType;
    balance = ZERO;
    locked = false;
    accountHistory = new ArrayList<>();
  }
}
