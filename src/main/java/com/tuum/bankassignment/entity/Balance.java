package com.tuum.bankassignment.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Balance implements Serializable {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private Currency currency;

    public Balance(Long accountId, BigDecimal amount, Currency currency) {
        this.accountId= accountId;
        this.amount = amount;
        this.currency = currency;
    }

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
}
