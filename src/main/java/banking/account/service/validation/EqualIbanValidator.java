package banking.account.service.validation;

import banking.account.service.domain.input.AccountTransferInput;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EqualIbanValidator implements ConstraintValidator<EqualIban, AccountTransferInput> {

  @Override
  public void initialize(EqualIban constraint) {
  }

  @Override
  public boolean isValid(AccountTransferInput input, ConstraintValidatorContext context) {
    return !input.getIban().equals(input.getReceivingIban());
  }
}
