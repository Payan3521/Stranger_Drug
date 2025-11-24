package com.desarrollox.backend_stranger_drug.api_notificaciones.exception;

public class NotificationNotFoundException extends RuntimeException{
    public NotificationNotFoundException(String message){
        super(message);
    }
}
