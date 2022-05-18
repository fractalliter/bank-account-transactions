package com.tuum.bankassignment.service;

import com.tuum.bankassignment.dto.CreateTransactionDTO;
import com.tuum.bankassignment.entity.Transaction;
import org.apache.ibatis.javassist.NotFoundException;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(CreateTransactionDTO transaction) throws NotFoundException;
}
