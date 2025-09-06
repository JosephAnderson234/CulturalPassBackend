package com.culturalpass.culturalpass.Event.exceptions;

public class EventAlreadyExistsException extends RuntimeException {
    public EventAlreadyExistsException(String message) {
        super(message);
    }
}