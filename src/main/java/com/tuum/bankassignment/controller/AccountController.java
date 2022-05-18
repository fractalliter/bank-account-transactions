package com.tuum.bankassignment.controller;

import com.tuum.bankassignment.dto.CreateAccountDTO;
import com.tuum.bankassignment.entity.Account;
import com.tuum.bankassignment.exception.AccountNotFoundException;
import com.tuum.bankassignment.exception.InvalidCurrencyException;
import com.tuum.bankassignment.service.AccountService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    AccountService accountService;

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
