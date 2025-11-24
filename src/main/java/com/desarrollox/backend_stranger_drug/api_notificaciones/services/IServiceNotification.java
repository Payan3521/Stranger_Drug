package com.desarrollox.backend_stranger_drug.api_notificaciones.services;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend_stranger_drug.api_notificaciones.controller.NotificationDto;
import com.desarrollox.backend_stranger_drug.api_notificaciones.models.Notification;

public interface IServiceNotification {

    Notification send(NotificationDto notificationDto);

    List<Notification> receive(Long receiveUserId);

    Optional<Notification> delete(Long id);

    Void clear(Long receiveUserId);
}
