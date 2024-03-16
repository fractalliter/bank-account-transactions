package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.*;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InvalidCurrencyException;
import com.earl.bank.mapper.AccountMapper;
import com.earl.bank.mapper.BalanceMapper;
import com.earl.bank.mapper.TransactionMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AccountServiceTest {
    @Autowired
    AccountService accountService;

    @Mock
    AccountMapper accountMapper;

    @Mock
    BalanceMapper balanceMapper;

    @Mock
    TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountMapper, balanceMapper);
    }

    @Test
    void createAccount() throws InvalidCurrencyException {
        // Given
        Set<Currency> currencies = Set.of(Currency.EUR, Currency.GBP);
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(currencies);
        accountDTO.setCustomerId("1234");
        Account accountModel = new Account(accountDTO.getCustomerId(), accountDTO.getCountry());
        accountModel.setAccountId(1L);
        Balance euroBalance = new Balance(accountModel.getAccountId(), BigDecimal.ZERO, Currency.EUR);
        euroBalance.setAccountId(1L);
        Balance poundBalance = new Balance(accountModel.getAccountId(), BigDecimal.ZERO, Currency.GBP);
        euroBalance.setAccountId(2L);

        // When
        when(accountMapper.createAccount(any(Account.class))).thenReturn(accountModel);
        when(balanceMapper.createBalance(any(Balance.class))).thenReturn(euroBalance, poundBalance);

        // Then
        var account = accountService.createAccount(accountDTO);
        assertNotNull(account);
        Assertions.assertEquals("Iran", account.getCountry());
        Assertions.assertEquals("1234", account.getCustomerId());
        Assertions.assertEquals(2, account.getBalances().size());
    }

    @Test
    void getAccount() throws AccountNotFoundException {
        var account = new Account("1234", "Iran");
        account.setAccountId(1L);

        when(accountMapper.getAccount(account.getAccountId())).thenReturn(account);

        account = accountService.getAccount(account.getAccountId());
        assertNotNull(account);
        assertEquals("1234", account.getCustomerId());
        assertEquals("Iran", account.getCountry());
        assertNotEquals(0L, account.getAccountId());
    }

    @Test
    void getAccountThrowsAccountNotFoundException() {
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
        assertNotEquals(0L, transaction.getId());
        assertEquals(0, transaction.getAmount().compareTo(new BigDecimal("1234.34")));

        var transactions = accountService.getTransactions(account.getAccountId(), (short) 0, (short) 2);
        assertNotEquals(0, transactions.size());
        assertEquals(1, transactions.size());
        Assertions.assertEquals(transactions.get(0).getAccountId(), account.getAccountId());
        Assertions.assertEquals(0, transactions.get(0).getAmount().compareTo(new BigDecimal("1234.34")));
        Assertions.assertEquals("test", transactions.get(0).getDescription());
        Assertions.assertEquals(Currency.EUR, transactions.get(0).getCurrency());
        Assertions.assertEquals(Direction.IN, transactions.get(0).getDirection());

        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.getTransactions(10000L, (short) 0, (short) 2));
    }
}