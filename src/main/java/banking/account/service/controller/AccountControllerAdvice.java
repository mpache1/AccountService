package banking.account.service.controller;

import banking.account.service.exception.AccountLockedException;
import banking.account.service.exception.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

import static java.util.List.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.LOCKED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class AccountControllerAdvice {

  private MessageSource messageSource;

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException exception) {
    log.info(exception.getMessage());
    return ResponseEntity.status(NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(AccountLockedException.class)
  public ResponseEntity<String> handleAccountLockedException(AccountLockedException exception) {
    log.info(exception.getMessage());
    return ResponseEntity.status(LOCKED).body(exception.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception) {
    String message = messageSource.getMessage("iban.already.assigned", of().toArray(), Locale.ENGLISH);
    log.info(exception.getMessage());
    return ResponseEntity.status(CONFLICT).body(message);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException exception) {
    log.info(exception.getMessage());
    return ResponseEntity.status(BAD_REQUEST).body(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
  }

  @Autowired
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }
}
