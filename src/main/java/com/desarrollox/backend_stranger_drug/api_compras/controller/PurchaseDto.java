package com.desarrollox.backend_stranger_drug.api_compras.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseDto {
    @NotNull(message = "El id del usuario es obligatorio")
    @Min(value = 1, message = "El id del usuario debe ser mayor a 0")
    private Long buyerUserId;

    @NotNull(message = "El id del video es obligatorio")
    @Min(value = 1, message = "El id del video debe ser mayor a 0")
    private Long videoId;

    @NotNull(message = "El precio pagado es obligatorio")
    @Min(value = 1, message = "El precio pagado debe ser mayor a 0")
    private double pricePaid;
}