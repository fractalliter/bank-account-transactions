package com.earl.bank.integration;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Currency;
import com.earl.bank.entity.Direction;
import com.earl.bank.logging.AOPLogging;
import com.earl.bank.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransactionTest {

    @Autowired
    AccountService accountService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AOPLogging aopLoggingMock;
    Account account;
    @Autowired
    private MockMvc mvc;

    static Stream<Arguments> validationDataForInvalidAccountId() throws CloneNotSupportedException {
        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrency(Currency.EUR);
        transactionDTO.setDescription("test");
        transactionDTO.setDirection(Direction.IN);
        List<Arguments> args = new LinkedList<>();
        transactionDTO.setAccountId(null);
        args.add(Arguments.arguments(
                "accountId", "Account id should not be null",
                transactionDTO.clone())
        );
        transactionDTO.setAccountId(-100L);
        args.add(Arguments.arguments(
                "accountId", "Account Id should be more than 0",
                transactionDTO.clone())
        );
        return args.stream();
    }

    static Stream<Arguments> validationData() throws CloneNotSupportedException {
        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrency(Currency.EUR);
        transactionDTO.setDescription("test");
        transactionDTO.setDirection(Direction.IN);
        List<Arguments> args = new LinkedList<>();
        transactionDTO.setAmount(new BigDecimal(-100));
        args.add(Arguments.arguments("amount", "Amount should be more than 0.00", transactionDTO.clone()));
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrency(null);
        args.add(Arguments.arguments("currency", "Currency should be selected", transactionDTO.clone()));
        transactionDTO.setCurrency(Currency.EUR);
        transactionDTO.setDirection(null);
        args.add(Arguments.arguments("direction", "Direction of the transaction should be selected", transactionDTO.clone()));
        return args.stream();
    }

    @BeforeEach
    void setUp() {
        doNothing().when(aopLoggingMock).afterCreateTransaction(any(), any());
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.EUR, Currency.GBP));
        accountDTO.setCustomerId("1234");
        account = accountService.createAccount(accountDTO);
    }

    @Test
    void loadContext() {
        assertNotNull(mvc);
        assertNotNull(objectMapper);
        assertNotNull(aopLoggingMock);
    }

    @Test
    void createTransaction() throws Exception {
        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrency(Currency.EUR);
        transactionDTO.setDescription("test");
        transactionDTO.setDirection(Direction.IN);

        mvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO))
                )
                .andExpect(status().isCreated());
    }

    @ParameterizedTest(name = "For field {0} should return {1}")
    @MethodSource("validationDataForInvalidAccountId")
    @DisplayName("Create Account Validations")
    public void createTransactionForInvalidAccountIdValidations(String field, String message, CreateTransactionDTO transactionDTO) throws Exception {

        mvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(String.format("$.%s", field)).value(message));
    }

    @ParameterizedTest(name = "For field {0} should return {1}")
    @MethodSource("validationData")
    @DisplayName("Create Account Validations")
    public void createTransactionValidations(String field, String message, CreateTransactionDTO transactionDTO) throws Exception {
        transactionDTO.setAccountId(account.getAccountId());
        mvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(String.format("$.%s", field)).value(message));
    }

    @Test
    @Disabled
    void getTransactions() {
    }
}
