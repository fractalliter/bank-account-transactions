package com.earl.bank.dto;

import com.earl.bank.entity.Currency;
import com.earl.bank.entity.Direction;

import java.math.BigDecimal;

public class CreateTransactionDTO {
    Long accountId;
    BigDecimal amount;
    Currency currency;
    Direction direction;
    String Description;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
