package com.earl.bank.integration;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Currency;
import com.earl.bank.logging.AOPLogging;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AOPLogging aopLoggingMock;
    @Autowired
    private MockMvc mvc;

    static Stream<Arguments> validationData() throws CloneNotSupportedException {
        var account = new CreateAccountDTO();
        account.setCustomerId("1234");
        account.setCountry("Iran");
        List<Arguments> args = new LinkedList<>();
        account.setCurrency(Set.of());
        args.add(Arguments.arguments(
                "currency", "At least one currency should be selected",
                account.clone())
        );
        account.setCurrency(Set.of(Currency.EUR, Currency.GBP));
        account.setCountry("");
        args.add(Arguments.arguments("country", "Country can not be empty", account.clone()));
        account.setCountry("Germany");
        account.setCustomerId("");
        args.add(Arguments.arguments("customerId", "Customer Id is can not be empty", account.clone()));
        return args.stream();
    }

    @BeforeEach
    void setUp() {
        doNothing().when(aopLoggingMock).afterCreateAccount(any(), any());
    }

    @Test
    void loadContext() {
        assertNotNull(mvc);
        assertNotNull(objectMapper);
        assertNotNull(aopLoggingMock);
    }

    @Test
    public void createAccount() throws Exception {
        var account = new CreateAccountDTO();
        account.setCustomerId("1234");
        account.setCountry("Iran");
        account.setCurrency(Set.of(Currency.EUR, Currency.GBP));
        mvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account))
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(account.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].currency").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].amount").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].amount").value(0L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].currency").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].amount").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].amount").value(0L));
    }

    @ParameterizedTest(name = "For field {0} should return {1}")
    @MethodSource("validationData")
    @DisplayName("Create Account Validations")
    public void createAccountValidations(String field, String message, CreateAccountDTO account) throws Exception {
        mvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account))
                )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(String.format("$.%s", field)).value(message));
    }

    @Test
    public void getAccount() throws Exception {
        var account = new CreateAccountDTO();
        account.setCustomerId("1234");
        account.setCountry("Iran");
        account.setCurrency(Set.of(Currency.EUR, Currency.GBP));

        MvcResult result = mvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account))
                )
                .andExpect(status().isCreated()).andReturn();
        Account account1 = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);
        mvc.perform(get("/account/" + account1.getAccountId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(account.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].currency").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].amount").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[0].amount").value(0L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].currency").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].amount").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balances[1].amount").value(0L));
    }
}
