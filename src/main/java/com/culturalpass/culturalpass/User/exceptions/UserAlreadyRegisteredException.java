package com.culturalpass.culturalpass.User.exceptions;

public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}