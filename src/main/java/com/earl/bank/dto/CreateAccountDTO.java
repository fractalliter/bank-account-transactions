package com.earl.bank.dto;

import com.earl.bank.entity.Currency;

import java.util.Set;

public class CreateAccountDTO {
    String customerId;
    String Country;
    Set<Currency> currency;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public Set<Currency> getCurrency() {
        return currency;
    }

    public void setCurrency(Set<Currency> currency) {
        this.currency = currency;
    }
}
