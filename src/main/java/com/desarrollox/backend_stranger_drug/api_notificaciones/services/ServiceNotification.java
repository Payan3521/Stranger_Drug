package com.desarrollox.backend_stranger_drug.api_notificaciones.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.desarrollox.backend_stranger_drug.api_notificaciones.exception.NotificationNotFoundException;
import com.desarrollox.backend_stranger_drug.api_notificaciones.models.Notification;
import com.desarrollox.backend_stranger_drug.api_notificaciones.repositories.IRepositoryNotification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceNotification implements IServiceNotification{
    private final IRepositoryNotification repositoryNotification;

    @Override
    public Notification send(Notification notification) {
        Notification saved = repositoryNotification.save(notification);
        return saved;
    }

    @Override
    public List<Notification> receive(Long receiveUserId) {
        return repositoryNotification.findByReceiverUser(receiveUserId);
    }

    @Override
    public Optional<Notification> delete(Long id) {
        if (repositoryNotification.existsById(id)) {
            Optional<Notification> notification = repositoryNotification.findById(id);
            repositoryNotification.delete(notification.get());
            return Optional.of(notification.get());
        }else{
            throw new NotificationNotFoundException("La notificación con id: " + id + " no fue encontrada, por lo que no se pudo eliminar");
        }
    }

    @Override
    public Void clear(Long receiveUserId) {
        return repositoryNotification.deleteAllByReceiverUserId(receiveUserId);
    }

}