package com.earl.bank.exception;

import com.earl.bank.entity.Currency;
import com.earl.bank.entity.Direction;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<?> accountNotFoundException(AccountNotFoundException accountNotFoundException) {
        return new ResponseEntity<>(accountNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidCurrencyException.class)
    public ResponseEntity<?> invalidCurrencyException(InvalidCurrencyException invalidCurrencyException) {
        return new ResponseEntity<>(invalidCurrencyException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InsufficientFundException.class)
    public ResponseEntity<?> insufficientFundException(InsufficientFundException insufficientFundException) {
        return new ResponseEntity<>(insufficientFundException.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<?> handleConverterErrors(JsonMappingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleConverterErrors(HttpMessageNotReadableException exception) {
        var message = Optional.ofNullable(exception.getMessage())
                .filter(Predicate.not(String::isEmpty)).map(msg -> {
                    if (msg.contains("entity.Currency")){
                        return String.format(
                                "Invalid Currency, should be of %s",
                                Arrays.toString(Currency.values())
                        );
                    } else if (msg.contains("entity.Direction")) {
                        return String.format(
                                "Invalid Direction, should be one of %s",
                                Arrays.toString(Direction.values())
                        );
                    }
                    return null;
                });
        return new ResponseEntity<>(message.orElse("Bad Request"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> databaseConnectionFailsException(Exception exception) {
        logger.info(exception.getMessage());
        return new ResponseEntity<>("There is a problem", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
