package com.desarrollox.backend_stranger_drug.api_compras.exception;

public class PurchaseNotFoundException extends RuntimeException{
    public PurchaseNotFoundException(String message){
        super(message);
    }
}