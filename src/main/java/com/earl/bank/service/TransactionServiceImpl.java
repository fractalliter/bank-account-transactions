package com.earl.bank.service;

import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Balance;
import com.earl.bank.entity.Direction;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InsufficientFundException;
import com.earl.bank.exception.InvalidAmountException;
import com.earl.bank.mapper.BalanceMapper;
import com.earl.bank.mapper.TransactionMapper;
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
    private Direction direction;

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper, BalanceMapper balanceMapper, AccountService accountService) {
        this.transactionMapper = transactionMapper;
        this.balanceMapper = balanceMapper;
        this.accountService = accountService;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Transaction createTransaction(CreateTransactionDTO transaction)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundException {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
        var account = accountService.getAccount(transaction.getAccountId());
        this.setDirection(transaction.getDirection());
        Balance newBalance = this.handleTransaction(account, transaction);
        var newTransaction = new Transaction(
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDirection(),
                transaction.getDescription()
        );
        Transaction persitedTransaction = transactionMapper.createTransaction(newTransaction);
        persitedTransaction.setBalance(newBalance);
        return persitedTransaction;
    }

    private Balance handleTransaction(Account account, CreateTransactionDTO transaction) {
        return this.direction.handler(balanceMapper, account, transaction);
    }
}
