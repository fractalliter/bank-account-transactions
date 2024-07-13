package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Balance;
import com.earl.bank.entity.Currency;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InvalidCurrencyException;
import com.earl.bank.mapper.AccountMapper;
import com.earl.bank.mapper.BalanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;

    @Autowired
    public AccountServiceImpl(AccountMapper accountMapper, BalanceMapper balanceMapper) {
        this.accountMapper = accountMapper;
        this.balanceMapper = balanceMapper;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Account createAccount(CreateAccountDTO account) throws InvalidCurrencyException {
        var tempCurrency = new HashSet<>(Set.copyOf(account.getCurrency()));
        tempCurrency.removeAll(Arrays.stream(Currency.values()).collect(Collectors.toSet()));
        if (account.getCurrency().isEmpty() || !tempCurrency.isEmpty()) {
            throw new InvalidCurrencyException();
        }
        var newAccount = new Account(account.getCustomerId(), account.getCountry());
        accountMapper.createAccount(newAccount);
        account.getCurrency().forEach(currency -> {
            var balance = new Balance(newAccount.getAccountId(), BigDecimal.ZERO, currency);
            balanceMapper.createBalance(balance);
        });
        return getAccount(newAccount.getAccountId());
    }

    @Override
    public Account getAccount(Long accountId) throws AccountNotFoundException {
        var account = accountMapper.getAccount(accountId);
        if (account == null) throw new AccountNotFoundException();
        return account;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Transaction> getTransactions(Long accountId, Short page, Short size) throws AccountNotFoundException {
        getAccount(accountId);
        return accountMapper.getTransactions(accountId, page * size, size);
    }
}
