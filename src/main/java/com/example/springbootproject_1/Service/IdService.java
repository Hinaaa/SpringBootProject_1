package com.example.springbootproject_1.Service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService { //for Id Generation
    public String generateId() {
        return UUID.randomUUID().toString();  // Generates a random UUID as a String
    }
}
