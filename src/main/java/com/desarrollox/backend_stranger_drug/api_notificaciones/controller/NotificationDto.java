package com.desarrollox.backend_stranger_drug.api_notificaciones.controller;

import lombok.Data;

@Data
public class NotificationDto {
    private String message;
    private Long purchaseId;
    private Long sendUserId;
    private Long receiverUserId;
}
