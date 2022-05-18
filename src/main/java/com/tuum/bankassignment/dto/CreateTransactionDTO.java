package com.tuum.bankassignment.dto;

import com.tuum.bankassignment.entity.Currency;
import com.tuum.bankassignment.entity.Direction;

public class CreateTransactionDTO {
    Long accountId;
    Long amount;
    Currency currency;
    Direction direction;
    String Description;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
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
