package com.desarrollox.backend_stranger_drug.api_notificaciones.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificationDto {
    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 2, max = 255, message = "El mensaje debe tener entre 2 y 255 caracteres")
    private String message;

    @NotNull(message = "El id de la compra es obligatorio")
    @Min(value = 1, message = "El id de la compra debe ser mayor a 0")
    private Long purchaseId;

    @NotNull(message = "El id del usuario es obligatorio")
    @Min(value = 1, message = "El id del usuario que envia la notificacion debe ser mayor a 0")
    private Long sendUserId;

    @NotNull(message = "El id del usuario es obligatorio")
    @Min(value = 1, message = "El id del usuario que recibe la notificacion debe ser mayor a 0")
    private Long receiverUserId;
}
