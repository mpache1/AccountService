package banking.account.service.controller;

import banking.account.service.domain.input.AccountDepositInput;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.service.deposit.DepositService;
import banking.account.service.util.AccountUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(DepositController.class)
public class DepositControllerTest {

  private static final String IBAN = "DE90123456781234567890";
  private static final BigDecimal AMOUNT = new BigDecimal("100.00");

  @MockBean
  private DepositService depositServiceMock;

  @Autowired
  private MockMvc mvc;

  @Autowired
  private JacksonTester<AccountDepositInput> jsonRequestAccountImput;

  @Test
  public void putValidDeposit() throws Exception {
    AccountDepositInput accountInput = new AccountDepositInput(IBAN, AMOUNT);
    String expectedResult = IBAN + " received " + AccountUtils.formatBalanceForOutput(AMOUNT);

    MockHttpServletResponse response =
        mvc.perform(put("/v1/deposit").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(expectedResult);
  }

  @Test
  public void putInvalidIbanDeposit() throws Exception {
    AccountDepositInput accountInput = new AccountDepositInput("TEST-IBAN", AMOUNT);

    MockHttpServletResponse response =
        mvc.perform(put("/v1/deposit").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void putInvalidAmountDeposit() throws Exception {
    AccountDepositInput accountInput = new AccountDepositInput(IBAN, ZERO);

    MockHttpServletResponse response =
        mvc.perform(put("/v1/deposit").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
}
