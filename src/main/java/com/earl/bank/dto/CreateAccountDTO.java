package com.earl.bank.dto;

import com.earl.bank.entity.Currency;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class CreateAccountDTO {

    @NotBlank(message = "Customer Id is required")
    String customerId;

    @NotBlank(message = "Country is required")
    String country;

    @NotEmpty(message = "At least one currency should be in the list")
    Set<Currency> currency;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Currency> getCurrency() {
        return currency;
    }

    public void setCurrency(Set<Currency> currency) {
        this.currency = currency;
    }
}
