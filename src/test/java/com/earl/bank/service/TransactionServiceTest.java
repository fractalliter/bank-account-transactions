package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Currency;
import com.earl.bank.entity.Direction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InsufficientFundException;
import com.earl.bank.exception.InvalidAmountException;
import com.earl.bank.mapper.BalanceMapper;
import com.earl.bank.mapper.TransactionMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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

    static Stream<Arguments> currencyAndDirectionCombinator() {
        List<Arguments> args = new LinkedList<>();
        for (Currency currency : Currency.values()) {
            for (Direction direction : Direction.values())
                args.add(Arguments.arguments(currency, direction));
        }
        return args.stream();
    }

    @ParameterizedTest(name = "for currency {0}")
    @EnumSource(Currency.class)
    @DisplayName("Transaction deposits money into user account")
    void createTransactionDeposit(Currency currency) {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.values()));
        accountDTO.setCustomerId("1234");
        var account = accountService.createAccount(accountDTO);

        assertNotNull(account);

        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrency(currency);
        transactionDTO.setDescription("test");
        transactionDTO.setDirection(Direction.IN);
        var transaction = transactionService.createTransaction(transactionDTO);

        assertNotNull(transaction);
        assertNotNull(transaction.getBalance());
        Assertions.assertEquals(transaction.getBalance().getAmount().compareTo(new BigDecimal("1000.00")), 0);
        assertEquals(transaction.getAccountId(), account.getAccountId());
        Assertions.assertEquals(transaction.getBalance().getCurrency(), currency);
        Assertions.assertEquals(transaction.getDirection(), Direction.IN);
    }

    @ParameterizedTest(name = "for currency {0}")
    @EnumSource(Currency.class)
    @DisplayName("transaction withdraws money from user account")
    void createTransactionWithdraw(Currency currency) {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.values()));
        accountDTO.setCustomerId("1234");
        var account = accountService.createAccount(accountDTO);
        assertNotNull(account);

        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setAmount(new BigDecimal("100.00"));
        transactionDTO.setCurrency(currency);
        transactionDTO.setDescription("test");
        transactionDTO.setDirection(Direction.IN);
        var transaction = transactionService.createTransaction(transactionDTO);
        assertNotNull(transaction);

        transactionDTO.setAmount(new BigDecimal("10.00"));
        transactionDTO.setDirection(Direction.OUT);
        transaction = transactionService.createTransaction(transactionDTO);

        assertNotNull(transaction);
        assertNotNull(transaction.getBalance());
        Assertions.assertEquals(transaction.getBalance().getAmount().compareTo(new BigDecimal("90.00")), 0);
        assertEquals(transaction.getAccountId(), account.getAccountId());
        Assertions.assertEquals(transaction.getBalance().getCurrency(), currency);
        Assertions.assertEquals(transaction.getDirection(), Direction.OUT);
    }

    @ParameterizedTest(name = "for currency {0} and direction {1}")
    @MethodSource("currencyAndDirectionCombinator")
    @DisplayName("creating a transaction for non existence bank account throws AccountNotFoundException")
    void createTransactionThrowsAccountNotFoundException(Currency currency, Direction direction) {
        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrency(currency);
        transactionDTO.setDescription("test");
        transactionDTO.setDirection(direction);
        transactionDTO.setAccountId(1000000L);
        assertNotNull(transactionDTO);
        assertThrows(
                AccountNotFoundException.class,
                () -> transactionService.createTransaction(transactionDTO)
        );

    }

    @ParameterizedTest(name = "for currency {0}")
    @EnumSource(Currency.class)
    @DisplayName("creating a withdraw transaction more than currency balance throws InsufficientFundException")
    void createTransactionThrowsInsufficientFundException(Currency currency) {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.values()));
        accountDTO.setCustomerId("1234");
        var account = accountService.createAccount(accountDTO);

        assertNotNull(account);

        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setCurrency(currency);
        transactionDTO.setDescription("test");
        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setDirection(Direction.OUT);
        transactionDTO.setAmount(new BigDecimal("1000.00"));

        assertNotNull(transactionDTO);
        assertThrows(
                InsufficientFundException.class,
                () -> transactionService.createTransaction(transactionDTO)
        );

    }

    @ParameterizedTest(name = "for currency {0} and direction {1}")
    @MethodSource("currencyAndDirectionCombinator")
    @DisplayName("creating a transaction with negative amount throws InvalidAmountException")
    void createTransactionThrowsInvalidAmountException(Currency currency, Direction direction) {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.values()));
        accountDTO.setCustomerId("1234");
        var account = accountService.createAccount(accountDTO);
        assertNotNull(account);

        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setCurrency(currency);
        transactionDTO.setDescription("test");
        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setDirection(direction);
        transactionDTO.setAmount(new BigDecimal("10000.00").negate());

        assertNotNull(transactionDTO);
        assertThrows(
                InvalidAmountException.class,
                () -> transactionService.createTransaction(transactionDTO)
        );
    }


}