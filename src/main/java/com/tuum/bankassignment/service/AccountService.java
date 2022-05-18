package com.tuum.bankassignment.service;

import com.tuum.bankassignment.dto.CreateAccountDTO;
import com.tuum.bankassignment.dto.CreateTransactionDTO;
import com.tuum.bankassignment.entity.Account;
import com.tuum.bankassignment.entity.Transaction;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.tools.web.BadHttpRequest;

import java.util.List;

public interface AccountService {
    Account createAccount(CreateAccountDTO account) throws NotFoundException, BadHttpRequest;
    Account getAccount(Long accountId) throws NotFoundException;
    List<Transaction> getTransactions(Long accountId, Short page, Short size) throws NotFoundException;
}
