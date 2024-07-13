package com.earl.bank.logging;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Currency;
import com.earl.bank.entity.Direction;
import com.earl.bank.entity.Transaction;
import com.earl.bank.service.AccountService;
import com.earl.bank.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class AOPLoggingTest {

    @Autowired
    AOPLogging aopLoggingMock;

    @Autowired
    RabbitMQClient senderMock;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @BeforeEach
    void setUp() {
        doNothing().when(senderMock).send(any());
    }

    @AfterEach
    void tearDown() {
        clearAllCaches();
        reset(aopLoggingMock);
    }

    @Test
    void afterCreateAccount() {
        var createAccountDTO = new CreateAccountDTO();
        createAccountDTO.setCountry("DE");
        createAccountDTO.setCurrency(Set.of(Currency.EUR, Currency.USD));
        createAccountDTO.setCustomerId("13342");
        var account = accountService.createAccount(createAccountDTO);
        verify(aopLoggingMock).afterCreateAccount(any(), argThat(obj -> {
            Account acc = (Account) obj;
            assertNotNull(acc.getAccountId());
            assertEquals(acc.getCountry(), account.getCountry());
            assertEquals(acc.getCustomerId(), account.getCustomerId());
            assertEquals(acc.getCurrencies(), account.getCurrencies());
            return true;
        }));
    }

    @Test
    void afterCreateTransaction() {
        var createAccountDTO = new CreateAccountDTO();
        createAccountDTO.setCountry("DE");
        createAccountDTO.setCurrency(Set.of(Currency.EUR, Currency.USD));
        createAccountDTO.setCustomerId("13342");
        var account = accountService.createAccount(createAccountDTO);

        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO();
        createTransactionDTO.setCurrency(Currency.EUR);
        createTransactionDTO.setAmount(new BigDecimal("10.10"));
        createTransactionDTO.setAccountId(account.getAccountId());
        createTransactionDTO.setDirection(Direction.IN);
        createTransactionDTO.setDescription("test");

        transactionService.createTransaction(createTransactionDTO);

        verify(aopLoggingMock).afterCreateTransaction(any(), argThat(obj -> {
            Transaction transaction = (Transaction) obj;
            assertEquals(transaction.getAccountId(), account.getAccountId());
            assertEquals(transaction.getAmount(), createTransactionDTO.getAmount());
            assertEquals(transaction.getDirection(), createTransactionDTO.getDirection());
            assertEquals(transaction.getCurrency(), createTransactionDTO.getCurrency());
            assertEquals(transaction.getDescription(), createTransactionDTO.getDescription());
            return true;
        }));
    }
}
