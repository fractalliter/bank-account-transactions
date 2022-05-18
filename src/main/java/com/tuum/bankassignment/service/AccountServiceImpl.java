package com.tuum.bankassignment.service;

import com.tuum.bankassignment.dto.CreateAccountDTO;
import com.tuum.bankassignment.entity.Account;
import com.tuum.bankassignment.entity.Balance;
import com.tuum.bankassignment.entity.Transaction;
import com.tuum.bankassignment.mapper.AccountMapper;
import com.tuum.bankassignment.mapper.BalanceMapper;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    BalanceMapper balanceMapper;
    @Override
    @Transactional
    public Account createAccount(CreateAccountDTO account) throws NotFoundException, BadHttpRequest {
        if (account.getCurrency().size() == 0){
            throw new BadHttpRequest();
        }
        var newAccount = new Account(account.getCustomerId(), account.getCountry());
        accountMapper.createAccount(newAccount);
        account.getCurrency().forEach(currency -> {
            var balance = new Balance(newAccount.getAccountId(), 0L, currency);
            balanceMapper.createBalance(balance);
        });
        return getAccount(newAccount.getAccountId());
    }

    @Override
    public Account getAccount(Long accountId) throws NotFoundException {
        var account = accountMapper.getAccount(accountId);
        if (account != null) return account;
        else throw new NotFoundException("account not found");
    }

    @Transactional
    @Override
    public List<Transaction> getTransactions(Long accountId, Short page, Short size) throws NotFoundException {
        getAccount(accountId);
        return accountMapper.getTransactions(accountId, page * size, size);
    }
}
