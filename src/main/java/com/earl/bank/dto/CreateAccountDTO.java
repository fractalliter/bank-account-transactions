package com.earl.bank.dto;

import com.earl.bank.entity.Currency;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Setter
@Getter
public class CreateAccountDTO implements Cloneable {

    @NotBlank(message = "Customer Id is can not be empty")
    String customerId;
    @NotBlank(message = "Country can not be empty")
    String country;
    @NotEmpty(message = "At least one currency should be selected")
    Set<Currency> currency;

    @Override
    public CreateAccountDTO clone() throws CloneNotSupportedException {
        return (CreateAccountDTO) super.clone();
    }
}
