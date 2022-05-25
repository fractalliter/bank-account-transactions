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
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService{
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
    @Transactional
    public Transaction createTransaction(CreateTransactionDTO transaction)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundException {
        var account = accountService.getAccount(transaction.getAccountId());
        if (transaction.getAmount() < 0){
            throw new InvalidAmountException();
        }
        if (Direction.OUT.equals(transaction.getDirection()) &&  balanceMapper.getBalance(
                transaction.getAccountId(),
                transaction.getCurrency()
        ).getAmount().doubleValue() - transaction.getAmount() < 0){
            throw new InsufficientFundException();
        }
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
