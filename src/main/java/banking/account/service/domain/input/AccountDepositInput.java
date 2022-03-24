package banking.account.service.domain.input;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
public class AccountDepositInput extends AccountInput {

  @NotNull(message = "The amount should not be null")
  @Positive(message = "The amount should always be positive")
  BigDecimal amount;

  public AccountDepositInput(String iban, BigDecimal amount) {
    super(iban);
    this.amount = amount;
  }
}
