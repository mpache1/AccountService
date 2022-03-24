package banking.account.service.controller;

import banking.account.service.domain.input.AccountDepositInput;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.service.deposit.DepositService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Locale;

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;
import static java.util.List.*;
import static java.util.Locale.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
class DepositController {

  private DepositService depositService;
  private MessageSource messageSource;

  @PutMapping("/deposit")
  public ResponseEntity<String> deposit(@Valid @NotNull @RequestBody AccountDepositInput transaction) {
    depositService.deposit(transaction);
    String message = messageSource.getMessage("deposit.done", of(transaction.getIban(), formatBalanceForOutput(transaction.getAmount())).toArray(), ENGLISH);
    log.info(message);
    return ResponseEntity.status(HttpStatus.OK).body(message);
  }

  @Autowired
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Autowired
  public void setDepositService(DepositService depositService) {
    this.depositService = depositService;
  }
}
