package com.virtuslab.internship.exception;

public class EmptyBasketException extends RuntimeException {
    public EmptyBasketException(String message) {
        super(message);
    }
}
