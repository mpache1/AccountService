package banking.account.service.service.transfer;

import banking.account.service.domain.input.AccountTransferInput;

public interface TransferService {

  boolean transfer(AccountTransferInput accountTransferInput);
}
