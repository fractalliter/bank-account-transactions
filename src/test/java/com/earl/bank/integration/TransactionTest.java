package com.earl.bank.integration;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Currency;
import com.earl.bank.entity.Direction;
import com.earl.bank.logging.AOPLogging;
import com.earl.bank.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Set;

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
    private MockMvc mvc;

    @Autowired
    AccountService accountService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AOPLogging aopLoggingMock;

    @BeforeEach
    void setUp(){
        doNothing().when(aopLoggingMock).afterCreateTransaction(any(), any());
    }

    @Test
    void loadContext() {
        assertNotNull(mvc);
        assertNotNull(objectMapper);
        assertNotNull(aopLoggingMock);
    }

    @Test
    void createTransaction() throws Exception {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.EUR, Currency.GBP));
        accountDTO.setCustomerId("1234");
        var account = accountService.createAccount(accountDTO);

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

    @Test
    @Disabled
    void getTransactions() {
    }
}
