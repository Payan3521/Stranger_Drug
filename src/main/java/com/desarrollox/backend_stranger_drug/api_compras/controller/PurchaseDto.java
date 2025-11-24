package com.desarrollox.backend_stranger_drug.api_compras.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PurchaseDto {
    @NotNull(message = "El id del usuario es obligatorio")
    @Min(value = 1, message = "El id del usuario debe ser mayor a 0")
    private Long buyerUserId;

    @NotNull(message = "El url del video es obligatorio")
    @Size(min = 2, max = 255, message = "El url del video debe tener entre 2 y 255 caracteres")
    private String videoUrl;

    @NotNull(message = "El precio pagado es obligatorio")
    @Min(value = 1, message = "El precio pagado debe ser mayor a 0")
    private double pricePaid;
}