package com.earl.bank.entity;

import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.exception.InsufficientFundException;
import com.earl.bank.mapper.BalanceMapper;

import java.math.BigDecimal;

public enum Direction {
    IN {
        @Override
        public Balance handler(BalanceMapper balanceMapper, Account account, CreateTransactionDTO transaction) {
            return balanceMapper.increaseBalance(
                    account.getAccountId(),
                    transaction.getCurrency(),
                    transaction.getAmount()
            );
        }
    }, OUT {
        @Override
        public Balance handler(BalanceMapper balanceMapper, Account account, CreateTransactionDTO transaction) {
            var balanceComparison = balanceMapper.getBalance(
                    transaction.getAccountId(),
                    transaction.getCurrency()
            ).getAmount().subtract(transaction.getAmount()).compareTo(BigDecimal.ZERO);
            if (balanceComparison < 0)
                throw new InsufficientFundException();
            return balanceMapper.decreaseBalance(
                    account.getAccountId(),
                    transaction.getCurrency(),
                    transaction.getAmount()
            );
        }
    };

    public abstract Balance handler(BalanceMapper balanceMapper, Account account, CreateTransactionDTO transaction);
}
