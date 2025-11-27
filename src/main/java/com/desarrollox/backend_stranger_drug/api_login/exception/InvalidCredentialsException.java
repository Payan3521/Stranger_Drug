package com.desarrollox.backend_stranger_drug.api_login.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException {
    private final String email;

    public InvalidCredentialsException(String email) {
        super("Credenciales inválidas para el usuario: " + email);
        this.email = email;
    }

    public InvalidCredentialsException(String email, String message) {
        super(message);
        this.email = email;
    }
}
