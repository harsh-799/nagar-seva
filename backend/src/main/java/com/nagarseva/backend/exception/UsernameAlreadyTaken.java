package com.nagarseva.backend.exception;

public class UsernameAlreadyTaken extends RuntimeException {
    public UsernameAlreadyTaken(String message) {
        super(message);
    }
}
