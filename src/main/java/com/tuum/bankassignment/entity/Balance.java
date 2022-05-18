package com.tuum.bankassignment.entity;

import java.io.Serializable;

public class Balance implements Serializable {
    private Long id;
    private Long accountId;
    private Long amount;
    private Currency currency;

    public Balance() {
        super();
    }

    public Balance(Long accountId, Long amount, Currency currency) {
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
}
