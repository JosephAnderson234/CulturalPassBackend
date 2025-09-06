package com.culturalpass.culturalpass.Security.exceptions;

public class AdminRegistrationNotAllowedException extends RuntimeException {
    public AdminRegistrationNotAllowedException(String message) {
        super(message);
    }
}