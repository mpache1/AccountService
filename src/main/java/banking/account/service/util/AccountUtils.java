package banking.account.service.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class AccountUtils {

  public static String formatBalanceForOutput(BigDecimal value) {
    if (value != null) {
      return NumberFormat.getCurrencyInstance().format(value);
    }
    return null;
  }
}
