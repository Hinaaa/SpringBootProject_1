package com.example.springbootproject_1.Exceptions;
//custom exception class to handle todo not found error.
public class TodoNotFoundException extends RuntimeException { //
    public TodoNotFoundException(String message) {
        super(message);
    }
}
