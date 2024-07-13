package com.earl.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Insufficient Fund")
public class InsufficientFundException extends RuntimeException {
    public InsufficientFundException() {
        super("Insufficient fund");
    }

    public InsufficientFundException(String message) {
        super(message);
    }
}
