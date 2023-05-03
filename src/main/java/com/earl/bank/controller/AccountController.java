package com.earl.bank.controller;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InvalidCurrencyException;
import com.earl.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Account createBankAccount(@RequestBody CreateAccountDTO account) throws InvalidCurrencyException {
        return accountService.createAccount(account);
    }

    @GetMapping("{accountId}")
    Account getAccount(@PathVariable Long accountId) throws AccountNotFoundException {
        return accountService.getAccount(accountId);
    }
}
