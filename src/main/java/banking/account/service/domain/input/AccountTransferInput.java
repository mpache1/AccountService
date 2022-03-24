package banking.account.service.domain.input;

import banking.account.service.validation.EqualIban;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@EqualIban
public class AccountTransferInput extends AccountDepositInput {

  @NotNull(message = "Receiving IBAN should always be 22 characters long")
  @Size(min = 22, max = 22, message = "IBAN should always be 22 characters long")
  @Pattern(regexp="^$|[a-zA-Z ]+$", message="IBAN must not include special characters")
  String receivingIban;

  public AccountTransferInput(String iban, String receivingIban, BigDecimal amount) {
    super(iban, amount);
    this.receivingIban = receivingIban;
  }
}
