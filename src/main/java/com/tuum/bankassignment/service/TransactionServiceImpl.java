package com.tuum.bankassignment.service;

import com.tuum.bankassignment.dto.CreateTransactionDTO;
import com.tuum.bankassignment.entity.Direction;
import com.tuum.bankassignment.entity.Transaction;
import com.tuum.bankassignment.exception.AccountNotFoundException;
import com.tuum.bankassignment.exception.InsufficientFundException;
import com.tuum.bankassignment.exception.InvalidAmountException;
import com.tuum.bankassignment.mapper.BalanceMapper;
import com.tuum.bankassignment.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionMapper transactionMapper;
    private final BalanceMapper balanceMapper;
    private final AccountService accountService;

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper, BalanceMapper balanceMapper, AccountService accountService) {
        this.transactionMapper = transactionMapper;
        this.balanceMapper = balanceMapper;
        this.accountService = accountService;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Transaction createTransaction(CreateTransactionDTO transaction)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundException {
        var account = accountService.getAccount(transaction.getAccountId());
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException();
        }
        var balanceComparison = balanceMapper.getBalance(
                transaction.getAccountId(),
                transaction.getCurrency()
        ).getAmount().subtract(transaction.getAmount()).compareTo(BigDecimal.ZERO);
        if (Direction.OUT.equals(transaction.getDirection())) {
            if (balanceComparison < 0)
                throw new InsufficientFundException();
            else balanceMapper.decreaseBalance(
                    account.getAccountId(),
                    transaction.getCurrency(),
                    transaction.getAmount()
            );
        }
        else if(Direction.IN.equals(transaction.getDirection()))
            balanceMapper.increaseBalance(
                account.getAccountId(),
                transaction.getCurrency(),
                transaction.getAmount()
        );
        var balance = balanceMapper.getBalance(account.getAccountId(), transaction.getCurrency());
        var newTransaction = new Transaction(
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDirection(),
                transaction.getDescription()
        );
        transactionMapper.createTransaction(newTransaction);
        newTransaction.setBalance(balance);
        return newTransaction;
    }
}
