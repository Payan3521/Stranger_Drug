package com.desarrollox.backend_stranger_drug.api_modelos.exception;

public class ModelNotFoundException extends RuntimeException{
    public ModelNotFoundException(String message){
        super(message);
    }
}