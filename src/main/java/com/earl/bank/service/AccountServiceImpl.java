package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Balance;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.mapper.AccountMapper;
import com.earl.bank.mapper.BalanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
    public Account createAccount(CreateAccountDTO account) {
        var newAccount = new Account(account.getCustomerId(), account.getCountry());
        accountMapper.createAccount(newAccount);
        if (newAccount.getAccountId() == null) {
            return newAccount;
        }
        List<Balance> balances = account.getCurrency().stream().map(currency -> {
            var balance = new Balance(newAccount.getAccountId(), BigDecimal.ZERO, currency);
            balanceMapper.createBalance(balance);
            return balance;
        }).collect(Collectors.toList());
        newAccount.setBalances(balances);
        return newAccount;
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
        List<Transaction> transactions = accountMapper.getTransactions(accountId, page * size, size);
        if (transactions.isEmpty()) throw new AccountNotFoundException();
        return transactions;
    }
}
