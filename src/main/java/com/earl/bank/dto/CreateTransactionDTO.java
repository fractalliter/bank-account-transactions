package com.earl.bank.dto;

import com.earl.bank.entity.Currency;
import com.earl.bank.entity.Direction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateTransactionDTO implements Cloneable {
    @NotNull(message = "Account id should not be null")
    @Positive(message = "Account Id should be more than 0")
    Long accountId;
    @Positive(message = "Amount should be more than 0.00")
    BigDecimal amount;
    @NotNull(message = "Currency should be selected")
    Currency currency;
    @NotNull(message = "Direction of the transaction should be selected")
    Direction direction;
    String description;

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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public CreateTransactionDTO clone() throws CloneNotSupportedException {
        return (CreateTransactionDTO) super.clone();
    }
}
