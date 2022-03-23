package banking.account.service.controller;

import banking.account.service.exception.AccountLockedException;
import banking.account.service.exception.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class AccountControllerAdvice {

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException exception) {
    log.info(exception.getMessage());
    return ResponseEntity.status(BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(AccountLockedException.class)
  public ResponseEntity<String> handleAccountLockedException(AccountLockedException exception) {
    log.info(exception.getMessage());
    return ResponseEntity.status(LOCKED).body(exception.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception) {
    log.info(exception.getMessage());
    return ResponseEntity.status(BAD_REQUEST).body("IBAN is already assigned to another account");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException exception) {
    log.info(exception.getMessage());
    return ResponseEntity.status(BAD_REQUEST).body(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
  }
}
