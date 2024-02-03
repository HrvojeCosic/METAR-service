package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ExceptionDetails errorDetails = new ExceptionDetails(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("CET")));
        return new ResponseEntity<>(errorDetails, notFound);
    }
}