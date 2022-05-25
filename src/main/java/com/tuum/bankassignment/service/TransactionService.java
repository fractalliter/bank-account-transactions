package com.tuum.bankassignment.service;

import com.tuum.bankassignment.dto.CreateTransactionDTO;
import com.tuum.bankassignment.entity.Transaction;
import com.tuum.bankassignment.exception.AccountNotFoundException;
import com.tuum.bankassignment.exception.InsufficientFundException;
import com.tuum.bankassignment.exception.InvalidAmountException;

public interface TransactionService {
    Transaction createTransaction(CreateTransactionDTO transaction)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundException;
}
