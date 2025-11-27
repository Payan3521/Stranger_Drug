package com.desarrollox.backend_stranger_drug.api_login.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}