package com.example.springbootproject_1.Exceptions;

import java.time.Instant;

public record ExceptionMessage(String error,
                               Instant timestamp,
                               String httpStatus) {
}
