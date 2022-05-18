package com.tuum.bankassignment.controller;

import com.tuum.bankassignment.dto.CreateAccountDTO;
import com.tuum.bankassignment.entity.Account;
import com.tuum.bankassignment.service.AccountService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/account")
    Account createBankAccount(@RequestBody CreateAccountDTO account) throws NotFoundException, BadHttpRequest {
        return accountService.createAccount(account);
    }

    @GetMapping("/account/{accountId}")
    Account getAccount(@PathVariable Long accountId) throws NotFoundException {
        return accountService.getAccount(accountId);
    }
}
