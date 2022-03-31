package com.virtuslab.internship.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class, EmptyBasketException.class})
    ResponseEntity<Object> handleResourceNotFoundException(WebRequest request,
                                                           RuntimeException e) {
        var status = HttpStatus.BAD_REQUEST;
        var restApiError = new RestApiError(LocalDateTime.now(), status, e.getMessage());
        return handleExceptionInternal(e, restApiError, new HttpHeaders(), status, request);
    }

}
