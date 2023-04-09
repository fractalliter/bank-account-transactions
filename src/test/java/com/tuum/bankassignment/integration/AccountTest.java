package com.tuum.bankassignment.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.bankassignment.dto.CreateAccountDTO;
import com.tuum.bankassignment.entity.Account;
import com.tuum.bankassignment.entity.Currency;
import org.junit.jupiter.api.Test;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void loadContext() {
        assertNotNull(mvc);
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
        mvc.perform(get("/account/"+account1.getAccountId()))
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
