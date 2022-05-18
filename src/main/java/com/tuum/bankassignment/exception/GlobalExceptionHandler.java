package com.tuum.bankassignment.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity accountNotFoundException(AccountNotFoundException accountNotFoundException) {
        return new ResponseEntity("Account Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidCurrencyException.class)
    public ResponseEntity invalidCurrencyException(InvalidCurrencyException invalidCurrencyException) {
        return new ResponseEntity("Invalid Currency", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InsufficientFundException.class)
    public ResponseEntity insufficientFundException(InsufficientFundException insufficientFundException) {
        return new ResponseEntity("Insufficient funds", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JsonMappingException.class) // Or whatever exception type you want to handle
    public ResponseEntity handleConverterErrors(JsonMappingException exception) { // Or whatever exception type you want to handle
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class) // Or whatever exception type you want to handle
    public ResponseEntity handleConverterErrors(HttpMessageNotReadableException exception) {
        var message = exception.getMessage() !=null &&
                !exception.getMessage().isEmpty() &&
                exception.getMessage().contains("com.tuum.bankassignment.entity.Currency")?
                "Invalid Currency" : "Bad request";
        return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity databaseConnectionFailsException(Exception exception) {
        return new ResponseEntity<>("There is a problem", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
