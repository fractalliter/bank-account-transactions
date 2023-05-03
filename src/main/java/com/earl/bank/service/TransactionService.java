package com.earl.bank.service;

import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InsufficientFundException;
import com.earl.bank.exception.InvalidAmountException;

public interface TransactionService {
    Transaction createTransaction(CreateTransactionDTO transaction)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundException;
}
