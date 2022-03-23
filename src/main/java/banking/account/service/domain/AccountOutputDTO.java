package banking.account.service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOutputDTO {

  String iban;
  String associatedIban;
  String accountType;
  String balance;
  String lock;
  List<String> accountHistory;
}
