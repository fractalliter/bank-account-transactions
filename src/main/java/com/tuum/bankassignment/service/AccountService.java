package com.tuum.bankassignment.service;

import com.tuum.bankassignment.dto.CreateAccountDTO;
import com.tuum.bankassignment.entity.Account;
import com.tuum.bankassignment.entity.Transaction;
import com.tuum.bankassignment.exception.AccountNotFoundException;
import com.tuum.bankassignment.exception.InvalidCurrencyException;

import java.util.List;

public interface AccountService {
    Account createAccount(CreateAccountDTO account) throws InvalidCurrencyException;
    Account getAccount(Long accountId) throws AccountNotFoundException;
    List<Transaction> getTransactions(Long accountId, Short page, Short size) throws AccountNotFoundException;
}
