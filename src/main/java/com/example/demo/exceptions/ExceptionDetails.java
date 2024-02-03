package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ExceptionDetails(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {

}