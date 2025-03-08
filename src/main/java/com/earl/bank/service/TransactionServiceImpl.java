package com.earl.bank.service;

import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Balance;
import com.earl.bank.entity.Direction;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InsufficientFundException;
import com.earl.bank.exception.InvalidAmountException;
import com.earl.bank.mapper.BalanceMapper;
import com.earl.bank.mapper.TransactionMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionMapper transactionMapper;
    private final BalanceMapper balanceMapper;
    private final AccountService accountService;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Transaction createTransaction(CreateTransactionDTO transaction)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundException {
        if (Direction.OUT.equals(transaction.getDirection())) {
            var balanceComparison = balanceMapper
                    .getBalance(
                            transaction.getAccountId(),
                            transaction.getCurrency()
                    )
                    .map(Balance::getAmount)
                    .map(amount -> amount.subtract(transaction.getAmount()).compareTo(BigDecimal.ZERO))
                    .orElseThrow(AccountNotFoundException::new);
            if (balanceComparison < 0) throw new InsufficientFundException();
        }
        Transaction newTransaction = Transaction.builder()
                .accountId(transaction.getAccountId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .direction(transaction.getDirection())
                .description(transaction.getDescription())
                .build();
        transactionMapper.createTransaction(newTransaction);
        return newTransaction;
    }
}
