package com.earl.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
    private Long accountId;
    private String customerId;
    private String country;

    private Set<Currency> currencies;
    private List<Balance> balances;
    private Date createdAt;
}
