package banking.account.service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(String iban) {
    super("Account with IBAN " + iban + " could not be found");
  }
}
