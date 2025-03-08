package com.earl.bank.service;

import com.earl.bank.entity.Account;
import com.earl.bank.entity.Balance;
import com.earl.bank.entity.Direction;
import com.earl.bank.entity.Transaction;
import com.earl.bank.mapper.BalanceMapper;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Aspect
@Service
@AllArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private BalanceMapper balanceMapper;

    @Override
    @AfterReturning(
            value = "execution(* com.earl.bank.service.AccountService.createAccount(..))",
            returning = "account"
    )
    public void createBalance(Account account) {
        account.getCurrencies().forEach(currency -> {
            var balance = Balance.builder()
                    .accountId(account.getAccountId())
                    .amount(BigDecimal.ZERO)
                    .currency(currency)
                    .build();
            balanceMapper.createBalance(balance);
        });
    }

    @Override
    @AfterReturning(
            value = "execution(* com.earl.bank.service.TransactionService.createTransaction(..))",
            returning = "transaction"
    )
    @Transactional()
    public void changeBalance(Transaction transaction) {
        if (Direction.OUT.equals(transaction.getDirection())) {
            balanceMapper.decreaseBalance(
                    transaction.getAccountId(),
                    transaction.getCurrency(),
                    transaction.getAmount()
            );
        } else if (Direction.IN.equals(transaction.getDirection()))
            balanceMapper.increaseBalance(
                    transaction.getAccountId(),
                    transaction.getCurrency(),
                    transaction.getAmount()
            );
    }
}
