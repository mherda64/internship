package com.virtuslab.internship.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record RestApiError(
        LocalDateTime timestamp,
        HttpStatus status,
        String message
) {
}
