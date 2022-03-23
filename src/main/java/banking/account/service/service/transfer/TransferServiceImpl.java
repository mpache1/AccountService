package banking.account.service.service.transfer;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.input.AccountTransferInput;
import banking.account.service.exception.AccountLockedException;
import banking.account.service.repository.AccountRepository;
import banking.account.service.service.AccountLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransferServiceImpl implements TransferService {

  private AccountRepository accountRepository;
  private AccountLoaderService accountLoaderService;

  private List<TransferExecuter> transferExecuters;

  private final Map<Class<? extends Account>, TransferExecuter> transferExecuterMap = new HashMap<>();

  @PostConstruct
  public void init() {
    for (TransferExecuter transferExecuter : transferExecuters) {
      transferExecuterMap.put(transferExecuter.appliesTo(), transferExecuter);
    }
  }

  @Override
  public boolean transfer(AccountTransferInput accountInput) {
    Account sending = loadAndCheckAccount(accountInput.getIban());
    Account receiving = loadAndCheckAccount(accountInput.getReceivingIban());
    boolean transferPossible = transferExecuterMap.get(sending.getClass()).transfer(sending, receiving, accountInput.getAmount());
    if (transferPossible) {
      accountRepository.save(sending);
      accountRepository.save(receiving);
    }
    return transferPossible;
  }

  private Account loadAndCheckAccount(String iban) {
    Account account = accountLoaderService.loadAccountByIban(iban);
    if (account.isLocked()) {
      throw new AccountLockedException(account.getIban());
    }
    return account;
  }

  @Autowired
  public void setAccountLoaderService(AccountLoaderService accountLoaderService) {
    this.accountLoaderService = accountLoaderService;
  }

  @Autowired
  public void setTransferExecuters(List<TransferExecuter> transferExecuters) {
    this.transferExecuters = transferExecuters;
  }

  @Autowired
  public void setAccountRepository(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
}
