package com.culturalpass.culturalpass.Event.exceptions;

public class MissingEventFieldException extends RuntimeException {
    public MissingEventFieldException(String message) {
        super(message);
    }
}