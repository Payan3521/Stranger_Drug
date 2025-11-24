package com.desarrollox.backend_stranger_drug.api_registro.exception;

public class InvalidAgeException extends RuntimeException{
    public InvalidAgeException(String message){
        super(message);
    }
}