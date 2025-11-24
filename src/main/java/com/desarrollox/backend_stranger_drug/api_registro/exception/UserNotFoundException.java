package com.desarrollox.backend_stranger_drug.api_registro.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
}