package com.desarrollox.backend_stranger_drug.api_notificaciones.services;

import java.util.List;
import java.util.Optional;

import com.desarrollox.backend_stranger_drug.api_notificaciones.models.Notification;

public interface IServiceNotification {

    Notification send(Notification notification);
    List<Notification> receive(Long receiveUserId);
    Optional<Notification> delete(Long id);
    Void clear(Long receiveUserId);
}
