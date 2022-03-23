package banking.account.service.exception;

public class AccountLockedException extends RuntimeException{

  public AccountLockedException(String iban) {
    super("Account: " +iban + " is currently locked. No transactions possible");
  }
}
