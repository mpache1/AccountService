package banking.account.service.validation;

import banking.account.service.domain.input.AccountTransferInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EqualIbanValidatorTest {

  private static final String IBAN = "DE90123456781234567890";

  private Validator validator;

  @BeforeEach
  public void setUp() throws Exception {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @Test
  public void shouldMarkPasswordsAsInvalid() {
    AccountTransferInput transferInput = new AccountTransferInput(IBAN, IBAN, ONE);
    Set<ConstraintViolation<AccountTransferInput>> violations = validator.validate(transferInput);
    assertEquals(1, violations.size());
  }
}
