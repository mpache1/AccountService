package banking.account.service.controller;

import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.input.AccountTransferInput;
import banking.account.service.service.transfer.TransferService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(TransferController.class)
public class TransferControllerTest {

  private static final String SENDING_IBAN = "DE90123456781234567890";
  private static final String RECEIVING_IBAN = "DE90123456781234567891";
  private static final BigDecimal AMOUNT = new BigDecimal("100.00");

  @MockBean
  private TransferService transferServiceMock;
  @Autowired
  private MockMvc mvc;
  @Autowired
  private JacksonTester<AccountTransferInput> jsonRequestAccountImput;

  @Test
  public void putValidTransfer() throws Exception {
    AccountTransferInput accountInput = new AccountTransferInput(SENDING_IBAN, RECEIVING_IBAN, AMOUNT);
    String expectedResult = SENDING_IBAN + " transfered " + AccountUtils.formatBalanceForOutput(AMOUNT) + " to " + RECEIVING_IBAN;

    given(transferServiceMock.transfer(eq(accountInput))).willReturn(true);

    MockHttpServletResponse response =
        mvc.perform(put("/v1/transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(expectedResult);
  }

  @Test
  public void putTransferFails() throws Exception {
    AccountTransferInput accountInput = new AccountTransferInput(SENDING_IBAN, RECEIVING_IBAN, AMOUNT);
    String expectedResult = "Transfering " + AccountUtils.formatBalanceForOutput(AMOUNT) + " from " + SENDING_IBAN + " to " + RECEIVING_IBAN + " was not possible";

    given(transferServiceMock.transfer(eq(accountInput))).willReturn(false);

    MockHttpServletResponse response =
        mvc.perform(put("/v1/transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    then(response.getContentAsString()).isEqualTo(expectedResult);
  }

  @Test
  public void putInvalidIbanTransfer() throws Exception {
    AccountTransferInput accountInput = new AccountTransferInput("TEST-IBAN", null, AMOUNT);

    MockHttpServletResponse response =
        mvc.perform(put("/v1/transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void putInvalidReceivingIbanTransfer() throws Exception {
    AccountTransferInput accountInput = new AccountTransferInput(SENDING_IBAN, "TEST_RECEIVING_IBAN", AMOUNT);

    MockHttpServletResponse response =
        mvc.perform(put("/v1/transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void putInvalidAmountTransfer() throws Exception {
    AccountTransferInput accountInput = new AccountTransferInput(SENDING_IBAN, RECEIVING_IBAN, ZERO);

    MockHttpServletResponse response =
        mvc.perform(put("/v1/transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
}