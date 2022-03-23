package banking.account.service.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EqualIbanValidator.class})
public @interface EqualIban  {

  String message() default "The sending IBAN can not be equal to the receiving IBAN";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

}