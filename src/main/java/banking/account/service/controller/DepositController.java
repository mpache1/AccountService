package banking.account.service.controller;

import banking.account.service.domain.input.AccountDepositInput;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.service.deposit.DepositService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
class DepositController {

  private DepositService depositService;

  @PutMapping("/deposit")
  public ResponseEntity<String> deposit(@Valid @NotNull @RequestBody AccountDepositInput transaction) {
    depositService.deposit(transaction);
    String result = transaction.getIban() + " received " + formatBalanceForOutput(transaction.getAmount());
    log.info(result);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @Autowired
  public void setDepositService(DepositService depositService) {
    this.depositService = depositService;
  }
}
