package com.example.springbootproject_1.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class globalExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public String handleRuntimeException(RuntimeException e) {
            return e.getMessage();
        }
}
