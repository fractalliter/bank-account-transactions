package com.tuum.bankassignment.controller;

import com.tuum.bankassignment.dto.CreateTransactionDTO;
import com.tuum.bankassignment.entity.Transaction;
import com.tuum.bankassignment.service.TransactionService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    Transaction createTransaction(@RequestBody CreateTransactionDTO transaction) throws NotFoundException {
        return transactionService.createTransaction(transaction);
    }
}
