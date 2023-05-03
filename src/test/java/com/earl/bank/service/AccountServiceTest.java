package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Currency;
import com.earl.bank.entity.Direction;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InvalidCurrencyException;
import com.earl.bank.mapper.AccountMapper;
import com.earl.bank.mapper.BalanceMapper;
import com.earl.bank.mapper.TransactionMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {
    @MockBean
    AccountService accountService;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    BalanceMapper balanceMapper;

    @Autowired
    TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountMapper, balanceMapper);
    }

    @Test
    void createAccount() throws InvalidCurrencyException {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.EUR, Currency.GBP));
        accountDTO.setCustomerId("1234");
        var account = accountService.createAccount(accountDTO);
        assertNotNull(account);
        Assertions.assertEquals(account.getCountry(), "Iran");
        Assertions.assertEquals(account.getCustomerId(), "1234");
        Assertions.assertEquals(account.getBalances().size(), 2);
        Assertions.assertEquals(account.getBalances().stream().mapToDouble(i -> i.getAmount().doubleValue()).sum(), 0d);
    }

    @Test
    void getAccount() throws AccountNotFoundException {
        var account = new Account("1234", "Iran");
        accountMapper.createAccount(account);
        account = accountService.getAccount(account.getAccountId());
        assertNotNull(account);
        assertEquals(account.getCustomerId(), "1234");
        assertEquals(account.getCountry(), "Iran");
        assertNotEquals(account.getAccountId(), 0L);
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(10000L));
    }

    @Test
    void getTransactions() throws AccountNotFoundException {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.EUR, Currency.GBP));
        accountDTO.setCustomerId("1234");
        var account = accountService.createAccount(accountDTO);
        var transaction = new Transaction(
                account.getAccountId(),
                new BigDecimal("1234.34"),
                Currency.EUR,
                Direction.IN,
                "test"
        );
        transactionMapper.createTransaction(transaction);
        assertNotNull(transaction);
        assertNotEquals(transaction.getId(), 0L);
        assertEquals(transaction.getAmount().compareTo(new BigDecimal("1234.34")), 0);
        var transactions = accountService.getTransactions(account.getAccountId(), (short) 0, (short) 2);
        assertNotEquals(transactions.size(), 0);
        assertEquals(transactions.size(), 1);
        Assertions.assertEquals(transactions.get(0).getAccountId(), account.getAccountId());
        Assertions.assertEquals(transactions.get(0).getAmount().compareTo(new BigDecimal("1234.34")), 0);
        Assertions.assertEquals(transactions.get(0).getDescription(), "test");
        Assertions.assertEquals(transactions.get(0).getCurrency(), Currency.EUR);
        Assertions.assertEquals(transactions.get(0).getDirection(), Direction.IN);

        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.getTransactions(10000L, (short) 0, (short) 2));
    }
}