package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InvalidCurrencyException;

import java.util.List;

public interface AccountService {
    Account createAccount(CreateAccountDTO account) throws InvalidCurrencyException;

    Account getAccount(Long accountId) throws AccountNotFoundException;

    List<Transaction> getTransactions(Long accountId, Short page, Short size) throws AccountNotFoundException;
}
