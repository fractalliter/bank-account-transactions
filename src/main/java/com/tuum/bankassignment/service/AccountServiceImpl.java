package com.tuum.bankassignment.service;

import com.tuum.bankassignment.dto.CreateAccountDTO;
import com.tuum.bankassignment.entity.Account;
import com.tuum.bankassignment.entity.Balance;
import com.tuum.bankassignment.entity.Currency;
import com.tuum.bankassignment.entity.Transaction;
import com.tuum.bankassignment.exception.AccountNotFoundException;
import com.tuum.bankassignment.exception.InvalidCurrencyException;
import com.tuum.bankassignment.mapper.AccountMapper;
import com.tuum.bankassignment.mapper.BalanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    @Transactional
    public Account createAccount(CreateAccountDTO account) throws InvalidCurrencyException {
        var tempCurrency = new HashSet<>(Set.copyOf(account.getCurrency()));
        tempCurrency.removeAll(Arrays.stream(Currency.values()).collect(Collectors.toSet()));
        if (account.getCurrency().size() == 0 || tempCurrency.size() !=0 ){
            throw new InvalidCurrencyException();
        }
        var newAccount = new Account(account.getCustomerId(), account.getCountry());
        accountMapper.createAccount(newAccount);
        account.getCurrency().forEach(currency -> {
            var balance = new Balance(newAccount.getAccountId(), new BigDecimal(0), currency);
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

    @Transactional
    @Override
    public List<Transaction> getTransactions(Long accountId, Short page, Short size) throws AccountNotFoundException {
        getAccount(accountId);
        return accountMapper.getTransactions(accountId, page * size, size);
    }
}
