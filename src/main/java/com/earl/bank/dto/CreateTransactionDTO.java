package com.earl.bank.dto;

import com.earl.bank.entity.Currency;
import com.earl.bank.entity.Direction;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Setter
@Getter
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

    @Override
    public CreateTransactionDTO clone() throws CloneNotSupportedException {
        return (CreateTransactionDTO) super.clone();
    }
}
