package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InvalidCurrencyException;
import com.earl.bank.mapper.AccountMapper;
import com.earl.bank.mapper.BalanceMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;

    @Override
    @Transactional()
    public Account createAccount(CreateAccountDTO account) throws InvalidCurrencyException {
        var newAccount = Account.builder()
                .customerId(account.getCustomerId())
                .country(account.getCountry())
                .build();
        accountMapper.createAccount(newAccount);
        newAccount.setCurrencies(account.getCurrency());
        return newAccount;
    }

    @Override
    public Account getAccount(Long accountId) throws AccountNotFoundException {
        return accountMapper.getAccount(accountId).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public List<Transaction> getTransactions(Long accountId, Short page, Short size) throws AccountNotFoundException {
        return accountMapper.getTransactions(accountId, page * size, size);
    }
}
