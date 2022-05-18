package com.tuum.bankassignment.service;

import com.tuum.bankassignment.dto.CreateTransactionDTO;
import com.tuum.bankassignment.entity.Direction;
import com.tuum.bankassignment.entity.Transaction;
import com.tuum.bankassignment.mapper.BalanceMapper;
import com.tuum.bankassignment.mapper.TransactionMapper;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    BalanceMapper balanceMapper;

    @Autowired
    AccountService accountService;
    @Override
    @Transactional
    public Transaction createTransaction(CreateTransactionDTO transaction) throws NotFoundException {
        var account = accountService.getAccount(transaction.getAccountId());
        if(transaction.getDirection().equals(Direction.IN))
            balanceMapper.increaseBalance(
                    account.getAccountId(),
                    transaction.getCurrency(),
                    transaction.getAmount()
            );
        else balanceMapper.decreaseBalance(
                account.getAccountId(),
                transaction.getCurrency(),
                transaction.getAmount()
        );
        var newTransaction = new Transaction(
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDirection(),
                transaction.getDescription()
        );
        transactionMapper.createTransaction(newTransaction);
        return transactionMapper.getTransaction(newTransaction.getId());
    }
}
