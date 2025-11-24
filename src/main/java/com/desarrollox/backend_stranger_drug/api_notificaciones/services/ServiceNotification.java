package com.desarrollox.backend_stranger_drug.api_notificaciones.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.desarrollox.backend_stranger_drug.api_compras.exception.PurchaseNotFoundException;
import com.desarrollox.backend_stranger_drug.api_compras.models.Purchase;
import com.desarrollox.backend_stranger_drug.api_compras.repositories.IRepositoryPurchase;
import com.desarrollox.backend_stranger_drug.api_notificaciones.controller.NotificationDto;
import com.desarrollox.backend_stranger_drug.api_notificaciones.exception.NotificationNotFoundException;
import com.desarrollox.backend_stranger_drug.api_notificaciones.models.Notification;
import com.desarrollox.backend_stranger_drug.api_notificaciones.repositories.IRepositoryNotification;
import com.desarrollox.backend_stranger_drug.api_registro.exception.UserNotFoundException;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;
import com.desarrollox.backend_stranger_drug.api_registro.repositories.IRepositoryRegistro;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceNotification implements IServiceNotification {
    private final IRepositoryNotification repositoryNotification;
    private final IRepositoryPurchase repositoryPurchase;
    private final IRepositoryRegistro repositoryRegistro;

    @Override
    public Notification send(NotificationDto notification) {
        if (!repositoryPurchase.existsById(notification.getPurchaseId())) {
            throw new PurchaseNotFoundException(
                    "La compra con id: " + notification.getPurchaseId() + " no fue encontrada");
        }
        if (!repositoryRegistro.existsById(notification.getSendUserId())) {
            throw new UserNotFoundException(
                    "El usuario con id: " + notification.getSendUserId() + " no fue encontrado");
        }
        if (!repositoryRegistro.existsById(notification.getReceiverUserId())) {
            throw new UserNotFoundException(
                    "El usuario con id: " + notification.getReceiverUserId() + " no fue encontrado");
        }
        Purchase purchase = repositoryPurchase.findById(notification.getPurchaseId()).get();
        User userSend = repositoryRegistro.findById(notification.getSendUserId()).get();
        User userReceive = repositoryRegistro.findById(notification.getReceiverUserId()).get();
        Notification notificationCreate = new Notification("message", purchase, userSend, userReceive);
        Notification saved = repositoryNotification.save(notificationCreate);
        return saved;
    }

    @Override
    public List<Notification> receive(Long receiveUserId) {
        return repositoryNotification.findByReceiverUser_id(receiveUserId);
    }

    @Override
    public Optional<Notification> delete(Long id) {
        if (repositoryNotification.existsById(id)) {
            Optional<Notification> notification = repositoryNotification.findById(id);
            repositoryNotification.delete(notification.get());
            return Optional.of(notification.get());
        } else {
            throw new NotificationNotFoundException(
                    "La notificación con id: " + id + " no fue encontrada, por lo que no se pudo eliminar");
        }
    }

    @Override
    @Transactional
    public Void clear(Long receiveUserId) {
        int count = repositoryNotification.deleteAllByReceiverUserId(receiveUserId);
        if (count == 0) {
            throw new NotificationNotFoundException(
                    "No se encontraron notificaciones para eliminar del usuario con id: " + receiveUserId);
        }
        return null;
    }

}