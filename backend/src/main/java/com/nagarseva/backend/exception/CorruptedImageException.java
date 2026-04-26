package com.nagarseva.backend.exception;

public class CorruptedImageException extends RuntimeException {
    public CorruptedImageException(String message) {
        super(message);
    }
}
