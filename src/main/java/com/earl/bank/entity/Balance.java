package com.earl.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Balance implements Serializable {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private Currency currency;
}
