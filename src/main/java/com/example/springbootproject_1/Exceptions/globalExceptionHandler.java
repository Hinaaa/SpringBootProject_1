package com.example.springbootproject_1.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class globalExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public String handleRuntimeException(RuntimeException e) {
            return e.getMessage();
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity <ExceptionMessage> handleException(Exception e) {
            ExceptionMessage error = new ExceptionMessage(
                    "Internal Server Error: " + e.getMessage(),
                    Instant.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.name()
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
