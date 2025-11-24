package com.desarrollox.backend_stranger_drug.api_notificaciones.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend_stranger_drug.api_notificaciones.exception.NotificationNotFoundException;
import com.desarrollox.backend_stranger_drug.api_notificaciones.models.Notification;
import com.desarrollox.backend_stranger_drug.api_notificaciones.services.IServiceNotification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class ControllerNotification {

    private final IServiceNotification serviceNotification;

    @PostMapping
    public ResponseEntity<Notification> send(@Valid @RequestBody NotificationDto notificationDto) {
        Notification saved = serviceNotification.send(notificationDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Notification>> receive(@PathVariable Long id) {
        List<Notification> listNotifications = serviceNotification.receive(id);
        if (listNotifications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listNotifications, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Notification> delete(@PathVariable Long id) {
        serviceNotification.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/clear/{id}")
    public ResponseEntity<Void> clear(@PathVariable Long id) {
        serviceNotification.clear(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
