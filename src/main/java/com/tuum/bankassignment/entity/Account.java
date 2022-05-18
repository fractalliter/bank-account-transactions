package com.tuum.bankassignment.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Account implements Serializable {
    private Long accountId;
    private String customerId;
    private String country;
    private List<Balance> balances = new LinkedList<>();

    public Account(String customerId, String country) {
        this.customerId = customerId;
        this.country = country;
    }

    public Account(String customerId, String country, List<Balance> balances) {
        super();
        this.customerId = customerId;
        this.country = country;
        this.balances = balances;
    }

    public Account() {
        super();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }
}
