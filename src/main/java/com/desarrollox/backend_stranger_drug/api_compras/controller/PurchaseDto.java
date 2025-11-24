package com.desarrollox.backend_stranger_drug.api_compras.controller;

import lombok.Data;

@Data
public class PurchaseDto {
    private Long buyerUserId;
    private String videoUrl;
    private double pricePaid;
}