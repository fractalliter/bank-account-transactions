package com.tuum.bankassignment.service;

import com.tuum.bankassignment.dto.CreateAccountDTO;
import com.tuum.bankassignment.dto.CreateTransactionDTO;
import com.tuum.bankassignment.entity.Account;
import com.tuum.bankassignment.entity.Currency;
import com.tuum.bankassignment.entity.Direction;
import com.tuum.bankassignment.exception.AccountNotFoundException;
import com.tuum.bankassignment.exception.InsufficientFundException;
import com.tuum.bankassignment.exception.InvalidAmountException;
import com.tuum.bankassignment.mapper.AccountMapper;
import com.tuum.bankassignment.mapper.BalanceMapper;
import com.tuum.bankassignment.mapper.TransactionMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
class TransactionServiceTest {

    @MockBean
    TransactionService transactionService;

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    BalanceMapper balanceMapper;
    @Autowired
    AccountService accountService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(transactionMapper, balanceMapper, accountService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createTransaction() throws InsufficientFundException, AccountNotFoundException, InvalidAmountException {
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
        var transaction = transactionService.createTransaction(transactionDTO);
        assertNotNull(transaction);
        assertNotNull(transaction.getBalance());
        assertEquals(transaction.getBalance().getAmount().compareTo(new BigDecimal("1000.00")), 0);
        assertEquals(transaction.getAccountId(), account.getAccountId());
        assertEquals(transaction.getBalance().getCurrency(), Currency.EUR);
        assertEquals(transaction.getDirection(), Direction.IN);

        transactionDTO.setAccountId(1000000L);
        assertThrows(AccountNotFoundException.class, ()-> transactionService.createTransaction(transactionDTO));

        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setDirection(Direction.OUT);
        transactionDTO.setAmount(new BigDecimal("10000.00"));
        assertThrows(InsufficientFundException.class, ()-> transactionService.createTransaction(transactionDTO));

        transactionDTO.setAmount(new BigDecimal("-1000.00"));
        assertThrows(InvalidAmountException.class, ()-> transactionService.createTransaction(transactionDTO));

        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setAmount(new BigDecimal("100.00"));
        transactionDTO.setCurrency(Currency.EUR);
        transactionDTO.setDescription("test");
        transactionDTO.setDirection(Direction.OUT);
        transaction = transactionService.createTransaction(transactionDTO);
        assertNotNull(transaction);
        assertNotNull(transaction.getBalance());
        assertEquals(transaction.getBalance().getAmount().compareTo(new BigDecimal(900L)), 0);
        assertEquals(transaction.getAccountId(), account.getAccountId());
        assertEquals(transaction.getBalance().getCurrency(), Currency.EUR);
        assertEquals(transaction.getDirection(), Direction.OUT);

    }
}