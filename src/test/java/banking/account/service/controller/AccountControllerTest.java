package banking.account.service.controller;

import banking.account.service.domain.Account;
import banking.account.service.domain.input.AccountInput;
import banking.account.service.domain.AccountOutputDTO;
import banking.account.service.domain.AccountType;
import banking.account.service.domain.CheckingAccount;
import banking.account.service.domain.PrivateLoanAccount;
import banking.account.service.domain.SavingsAccount;
import banking.account.service.domain.input.AccountSavingInput;
import banking.account.service.service.account.AccountService;
import banking.account.service.service.transformer.AccountOutputTransformer;
import banking.account.service.service.transformer.AccountTypesInputTransformer;
import banking.account.service.util.AccountUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static banking.account.service.domain.AccountType.SAVING;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

  private static final String IBAN = "DE90123456781234567890";
  private static final String ASSOCIATED_IBAN = "DE90123456781234567891";

  @MockBean
  private AccountService accountServiceMock;
  @MockBean
  AccountOutputTransformer accountOutputTransformerMock;
  @MockBean
  AccountTypesInputTransformer accountTypesInputTransformerMock;
  @Autowired
  private MockMvc mvc;
  @Autowired
  private JacksonTester<AccountInput> jsonAccountImput;
  @Autowired
  private JacksonTester<AccountSavingInput> jsonAccountSavingImput;
  @Autowired
  private JacksonTester<AccountOutputDTO> jsonAccountOutput;
  @Autowired
  private JacksonTester<List<AccountOutputDTO>> jsonAccountListOutput;
  @Autowired
  private JacksonTester<List<String>> jsonHistoryOutput;

  @Test
  public void postValidCreateChecking() throws Exception {
    AccountInput accountInput = new AccountInput(IBAN);
    CheckingAccount account = new CheckingAccount(IBAN);
    AccountOutputDTO accountOutput = new AccountOutputDTO(IBAN, null, AccountUtils.formatBalanceForOutput(ZERO), "CHECKING", "not locked", new ArrayList<>());

    given(accountServiceMock.createCheckingAccount(eq(accountInput))).willReturn(account);
    given(accountOutputTransformerMock.transformAccount(account)).willReturn(accountOutput);

    MockHttpServletResponse response =
        mvc.perform(post("/v1/create/checking").contentType(APPLICATION_JSON)
                        .content(jsonAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    then(response.getContentAsString()).isEqualTo(jsonAccountOutput.write(accountOutput).getJson());
  }

  @Test
  public void postValidCreateSaving() throws Exception {
    AccountSavingInput accountInput = new AccountSavingInput(IBAN, ASSOCIATED_IBAN);
    SavingsAccount account = new SavingsAccount(IBAN, ASSOCIATED_IBAN);
    AccountOutputDTO accountOutput = new AccountOutputDTO(IBAN, ASSOCIATED_IBAN, AccountUtils.formatBalanceForOutput(ZERO), "SAVING", "not locked", new ArrayList<>());

    given(accountServiceMock.createSavingAccount(eq(accountInput))).willReturn(account);
    given(accountOutputTransformerMock.transformAccount(account)).willReturn(accountOutput);

    MockHttpServletResponse response = mvc.perform(post("/v1/create/saving").contentType(APPLICATION_JSON)
                                                       .content(jsonAccountSavingImput.write(accountInput)
                                                                    .getJson())).andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    then(response.getContentAsString()).isEqualTo(jsonAccountOutput.write(accountOutput).getJson());
  }

  @Test
  public void postValidCreatePrivateLoan() throws Exception {
    AccountInput accountInput = new AccountInput(IBAN);
    PrivateLoanAccount account = new PrivateLoanAccount(IBAN);
    AccountOutputDTO accountOutput = new AccountOutputDTO(IBAN, null, AccountUtils.formatBalanceForOutput(ZERO), "PRIVATELOAN", "not locked", new ArrayList<>());

    given(accountServiceMock.createPrivateLoadAccount(eq(accountInput))).willReturn(account);
    given(accountOutputTransformerMock.transformAccount(account)).willReturn(accountOutput);

    MockHttpServletResponse response = mvc.perform(post("/v1/create/privateloan").contentType(APPLICATION_JSON)
                                                       .content(jsonAccountImput.write(accountInput)
                                                                    .getJson())).andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    then(response.getContentAsString()).isEqualTo(jsonAccountOutput.write(accountOutput).getJson());
  }

  @Test
  public void postInvalidIbanCreate() throws Exception {
    AccountInput accountInput = new AccountInput("TEST_IBAN");

    MockHttpServletResponse response =
        mvc.perform(post("/v1/create/privateloan").contentType(APPLICATION_JSON)
                        .content(jsonAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void postInvalidAssociatedIbanCreate() throws Exception {
    AccountSavingInput accountInput = new AccountSavingInput(IBAN, null);

    MockHttpServletResponse response =
        mvc.perform(post("/v1/create/saving").contentType(APPLICATION_JSON)
                        .content(jsonAccountImput.write(accountInput).getJson()))
            .andReturn().getResponse();

    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void putValidLock() throws Exception {
    AccountInput accountInput = new AccountInput(IBAN);
    String expected = "Account: " + IBAN + " has been locked";
    MockHttpServletResponse response = mvc.perform(put("/v1/lock").contentType(APPLICATION_JSON)
                                                       .content(jsonAccountImput.write(accountInput)
                                                                    .getJson())).andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(expected);
  }

  @Test
  public void putValidUnlock() throws Exception {
    AccountInput accountInput = new AccountInput(IBAN);
    String expected = "Account: " + IBAN + " has been unlocked";
    MockHttpServletResponse response = mvc.perform(put("/v1/unlock").contentType(APPLICATION_JSON)
                                                       .content(jsonAccountImput.write(accountInput)
                                                                    .getJson())).andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(expected);
  }

  @Test
  public void putInvalidUnlock() throws Exception {
    AccountInput accountInput = new AccountInput("TEST_IBAN");
    MockHttpServletResponse response = mvc.perform(put("/v1/unlock").contentType(APPLICATION_JSON)
                                                       .content(jsonAccountImput.write(accountInput)
                                                                    .getJson())).andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void putValidBalance() throws Exception {
    PrivateLoanAccount account = new PrivateLoanAccount(IBAN);
    given(accountServiceMock.findAccount(eq(IBAN))).willReturn(account);

    MockHttpServletResponse response = mvc.perform(get("/v1/{iban}/balance", IBAN).contentType(APPLICATION_JSON)
                                                       .content("")).andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(AccountUtils.formatBalanceForOutput(ZERO));
  }

  @Test
  public void putValidHistory() throws Exception {
    PrivateLoanAccount account = new PrivateLoanAccount(IBAN);
    given(accountServiceMock.findAccount(eq(IBAN))).willReturn(account);

    MockHttpServletResponse response = mvc.perform(get("/v1/{iban}/history", IBAN).contentType(APPLICATION_JSON)
                                                       .content("")).andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(jsonHistoryOutput.write(new ArrayList<>()).getJson());
  }

  @Test
  public void putValidAccounts() throws Exception {
    CheckingAccount checkingAccount = new CheckingAccount(ASSOCIATED_IBAN);
    SavingsAccount savingsAccount = new SavingsAccount(IBAN, ASSOCIATED_IBAN);

    AccountOutputDTO checkingAccountOutput = new AccountOutputDTO(ASSOCIATED_IBAN, null, AccountUtils.formatBalanceForOutput(ZERO), "CHECKING", "not locked", new ArrayList<>());
    AccountOutputDTO savingAccountOutput = new AccountOutputDTO(IBAN, ASSOCIATED_IBAN, AccountUtils.formatBalanceForOutput(ZERO), "SAVING", "not locked", new ArrayList<>());

    List<Account> accounts = List.of(savingsAccount, checkingAccount);
    List<AccountOutputDTO> accountOutputs = List.of(checkingAccountOutput, savingAccountOutput);

    given(accountServiceMock.getAccounts(new HashSet<>())).willReturn(accounts);
    given(accountOutputTransformerMock.transformAccounts(accounts)).willReturn(accountOutputs);

    MockHttpServletResponse response = mvc.perform(get("/v1/accounts").contentType(APPLICATION_JSON)
                                                       .content("")).andReturn().getResponse();

    response.setCharacterEncoding("UTF-8");
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(jsonAccountListOutput.write(accountOutputs).getJson());
  }

  @Test
  public void putValidAccountsWithTypes() throws Exception {
    SavingsAccount savingsAccount = new SavingsAccount(IBAN, ASSOCIATED_IBAN);
    CheckingAccount checkingAccount = new CheckingAccount(IBAN);
    AccountOutputDTO savingAccountOutput = new AccountOutputDTO(IBAN, ASSOCIATED_IBAN, AccountUtils.formatBalanceForOutput(ZERO), "SAVING", "not locked", new ArrayList<>());

    Set<AccountType> types = Set.of(SAVING);

    List<Account> accounts = List.of(savingsAccount, checkingAccount);
    List<AccountOutputDTO> accountOutputs = List.of(savingAccountOutput);

    given(accountServiceMock.getAccounts(types)).willReturn(accounts);
    given(accountOutputTransformerMock.transformAccounts(accounts)).willReturn(accountOutputs);
    given(accountTypesInputTransformerMock.transformAccountInput(anyList())).willReturn(types);

    MockHttpServletResponse response = mvc.perform(get("/v1/accounts?type=saving").contentType(APPLICATION_JSON)
                                                       .content("")).andReturn().getResponse();
    response.setCharacterEncoding("UTF-8");

    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(jsonAccountListOutput.write(accountOutputs).getJson());
  }
}