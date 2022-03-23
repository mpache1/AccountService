package banking.account.service.service;

import banking.account.service.domain.AccountType;
import banking.account.service.service.transformer.AccountTypesInputTransformer;
import banking.account.service.service.transformer.AccountTypesInputTransformerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;

import static banking.account.service.domain.AccountType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountTypesInputTransformerTest {

  AccountTypesInputTransformer underTest;

  @BeforeEach
  public void setUp() {
    underTest = new AccountTypesInputTransformerImpl();
  }

  @Test
  void transformAccountInput() {
    List<String> inputStrings = List.of("saving", "checking", "privateloan", "financing");
    List<AccountType> results = underTest.transformAccountInput(inputStrings);
    assertEquals(results.size(), 3);
    assertTrue(results.contains(SAVING));
    assertTrue(results.contains(CHECKING));
    assertTrue(results.contains(PRIVATELOAN));
  }
}