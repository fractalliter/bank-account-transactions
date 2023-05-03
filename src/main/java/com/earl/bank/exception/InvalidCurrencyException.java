package com.earl.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Currency")
public class InvalidCurrencyException extends RuntimeException {
    public InvalidCurrencyException() {
    }

    public InvalidCurrencyException(String message) {
        super(message);
    }
}
