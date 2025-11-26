package com.desarrollox.backend_stranger_drug.api_publicaciones.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }
}