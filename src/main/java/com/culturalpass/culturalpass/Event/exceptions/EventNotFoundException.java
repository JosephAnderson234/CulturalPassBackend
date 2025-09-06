package com.culturalpass.culturalpass.Event.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}