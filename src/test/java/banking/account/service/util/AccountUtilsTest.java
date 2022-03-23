package banking.account.service.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountUtilsTest {

  @Test
  public void formatBalanceForOutput() {
    String expected = "100,00 €";
    String result = AccountUtils.formatBalanceForOutput(new BigDecimal("100.00"));
    assertEquals(expected, result);
  }
}