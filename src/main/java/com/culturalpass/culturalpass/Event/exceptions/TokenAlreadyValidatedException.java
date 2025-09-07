package com.culturalpass.culturalpass.Event.exceptions;

public class TokenAlreadyValidatedException extends RuntimeException {
    public TokenAlreadyValidatedException(String message) {
        super(message);
    }
}
