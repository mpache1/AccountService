package banking.account.service.domain.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
public class AccountSavingInput extends AccountInput {

  @NotNull(message = "The associated IBAN should not be null")
  @Size(min = 22, max = 22, message = "Associated IBAN should always be 22 characters long")
  @Pattern(regexp="^$|[a-zA-Z ]+$", message="IBAN must not include special characters")
  String associatedIban;

  public AccountSavingInput(String iban, String associatedIban) {
    super(iban);
    this.associatedIban = associatedIban;
  }
}
