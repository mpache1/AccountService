package banking.account.service.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class AccountUtils {

  public static String formatBalanceForOutput(BigDecimal value) {
    if (value != null) {
      return NumberFormat.getCurrencyInstance(Locale.GERMANY).format(value);
    }
    return null;
  }
}
