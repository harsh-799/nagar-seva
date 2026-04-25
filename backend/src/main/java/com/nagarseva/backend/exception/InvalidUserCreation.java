package com.nagarseva.backend.exception;

public class InvalidUserCreation extends RuntimeException {
    public InvalidUserCreation(String message) {
        super(message);
    }
}
