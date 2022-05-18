package com.tuum.bankassignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Description Missing")
public class DescriptionMissingException extends RuntimeException{
}
