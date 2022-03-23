package banking.account.service.controller;

import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.input.AccountTransferInput;
import banking.account.service.service.transfer.TransferService;
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

import static banking.account.service.util.AccountUtils.formatBalanceForOutput;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransferController {

  private TransferService transferService;

  @PutMapping("/transfer")
  public ResponseEntity<String> transfer(@Valid @RequestBody AccountTransferInput accountTransferInput) {
    boolean transferPossible = transferService.transfer(accountTransferInput);
    String result = buildResult(accountTransferInput, transferPossible);
    log.info(result);
    if (transferPossible) {
      return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
  }

  private String buildResult(AccountTransferInput accountTransferInput, boolean transferPossile) {
    String formatedAmount = formatBalanceForOutput(accountTransferInput.getAmount());
    if (transferPossile) {
      return accountTransferInput.getIban() + " transfered " + formatedAmount + " to " + accountTransferInput.getReceivingIban();
    }
    return "Transfering " + formatedAmount + " from " + accountTransferInput.getIban() + " to " + accountTransferInput.getReceivingIban() + " was not possible";
  }

  @Autowired
  public void setTransferService(TransferService transferService) {
    this.transferService = transferService;
  }
}
