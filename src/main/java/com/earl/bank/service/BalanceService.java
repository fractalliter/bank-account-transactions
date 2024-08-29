package com.earl.bank.service;

import com.earl.bank.entity.Account;
import com.earl.bank.entity.Transaction;

public interface BalanceService {
    void createBalance(Account account);

    void changeBalance(Transaction transaction);
}
