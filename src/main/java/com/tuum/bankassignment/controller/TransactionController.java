package com.tuum.bankassignment.controller;

import com.tuum.bankassignment.dto.CreateTransactionDTO;
import com.tuum.bankassignment.entity.Transaction;
import com.tuum.bankassignment.exception.AccountNotFoundException;
import com.tuum.bankassignment.service.AccountService;
import com.tuum.bankassignment.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Transaction createTransaction(@RequestBody CreateTransactionDTO transaction) throws AccountNotFoundException {
        return transactionService.createTransaction(transaction);
    }
    @GetMapping("{accountId}/all")
    List<Transaction> getTransactions(
            @PathVariable("accountId") Long accountId,
            @RequestParam(value = "page", defaultValue = "0") Short page,
            @RequestParam(value = "size", defaultValue = "10") Short size){
        return accountService.getTransactions(accountId, page, size);
    }
}
