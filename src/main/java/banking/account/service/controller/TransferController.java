package banking.account.service.controller;

import banking.account.service.domain.input.AccountTransferInput;
import banking.account.service.service.transfer.TransferService;
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

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;
import static java.util.List.*;
import static java.util.Locale.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransferController {

  private TransferService transferService;
  private MessageSource messageSource;

  @PutMapping("/transfer")
  public ResponseEntity<String> transfer(@Valid @RequestBody AccountTransferInput transaction) {
    boolean transferPossible = transferService.transfer(transaction);
    String message = buildResult(transaction, transferPossible);
    log.info(message);
    if (transferPossible) {
      return ResponseEntity.status(HttpStatus.OK).body(message);
    }
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(message);
  }

  private String buildResult(AccountTransferInput input, boolean transferPossile) {
    String formatedAmount = formatBalanceForOutput(input.getAmount());
    if (transferPossile) {
      return messageSource.getMessage("transfer.done", of(input.getIban(), formatedAmount, input.getReceivingIban()).toArray(), ENGLISH);
    }
    return messageSource.getMessage("transfer.not.possible", of(formatedAmount, input.getIban(), input.getReceivingIban()).toArray(), ENGLISH);
  }

  @Autowired
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Autowired
  public void setTransferService(TransferService transferService) {
    this.transferService = transferService;
  }
}
