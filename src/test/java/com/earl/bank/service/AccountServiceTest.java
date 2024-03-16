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
import java.util.List;
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
        // Given
        var account = new Account("1234", "Iran");
        account.setAccountId(1L);

        // When
        when(accountMapper.getAccount(account.getAccountId())).thenReturn(account);

        // Then
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
        // Given
        var transaction = new Transaction(
                1L,
                new BigDecimal("1234.34"),
                Currency.EUR,
                Direction.IN,
                "test"
        );
        var transaction1 = new Transaction(
                1L,
                new BigDecimal("123.3"),
                Currency.GBP,
                Direction.OUT,
                "test1"
        );

        // When
        when(accountMapper.getTransactions(1L, 0, (short) 2))
                .thenReturn(List.of(transaction, transaction1));

        // Then
        var transactions = accountService.getTransactions(1L, (short) 0, (short) 2);
        assertEquals(2, transactions.size());
        Assertions.assertEquals(1L, transactions.get(0).getAccountId());
        Assertions.assertEquals(0, transactions.get(0).getAmount().compareTo(new BigDecimal("1234.34")));
        Assertions.assertEquals("test", transactions.get(0).getDescription());
        Assertions.assertEquals(Currency.EUR, transactions.get(0).getCurrency());
        Assertions.assertEquals(Direction.IN, transactions.get(0).getDirection());
    }

    @Test
    void getTransactionThrowsAccountNotFound(){
        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.getTransactions(10000L, (short) 0, (short) 2));
    }
}