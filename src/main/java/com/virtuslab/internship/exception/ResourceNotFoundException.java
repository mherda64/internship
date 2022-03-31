package com.virtuslab.internship.exception;

import java.util.NoSuchElementException;

public class ResourceNotFoundException extends NoSuchElementException {
    public ResourceNotFoundException(String s) {
        super(s);
    }
}
