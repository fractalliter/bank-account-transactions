package com.earl.bank.controller;

import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.service.AccountService;
import com.earl.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @Autowired
    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Transaction createTransaction(@RequestBody CreateTransactionDTO transaction) throws AccountNotFoundException {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("{accountId}/all")
    List<Transaction> getTransactions(
            @PathVariable("accountId") Long accountId,
            @RequestParam(value = "page", defaultValue = "0") Short page,
            @RequestParam(value = "size", defaultValue = "10") Short size) {
        return accountService.getTransactions(accountId, page, size);
    }
}
