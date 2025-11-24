package com.desarrollox.backend_stranger_drug.core.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.desarrollox.backend_stranger_drug.api_compras.exception.PurchaseNotFoundException;
import com.desarrollox.backend_stranger_drug.api_modelos.exception.ModelNotFoundException;
import com.desarrollox.backend_stranger_drug.api_registro.exception.InvalidAgeException;
import com.desarrollox.backend_stranger_drug.api_registro.exception.UserAlreadyRegisteredException;
import com.desarrollox.backend_stranger_drug.api_registro.exception.UserNotFoundException;
import com.desarrollox.backend_stranger_drug.api_secciones.exception.SectionAlreadyExistsException;
import com.desarrollox.backend_stranger_drug.api_secciones.exception.SectionNotFoundException;
import com.desarrollox.backend_stranger_drug.api_notificaciones.exception.NotificationNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler({
        UserNotFoundException.class,
        ModelNotFoundException.class,
        PurchaseNotFoundException.class,
        SectionNotFoundException.class,
        NotificationNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler({
        UserAlreadyRegisteredException.class,
        SectionAlreadyExistsException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Conflicto en la solicitud", ex.getMessage());
    }

    @ExceptionHandler(InvalidAgeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAge(InvalidAgeException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Edad inválida", ex.getMessage());
    }

}