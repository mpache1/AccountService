package banking.account.service.domain.input;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class AccountInput {

  @NotNull
  @Size(min = 22, max = 22, message = "IBAN should always be 22 characters long")
  @Pattern(regexp="^[a-zA-Z0-9_.-]*$", message="IBAN must not include special characters")
  String iban;
}
