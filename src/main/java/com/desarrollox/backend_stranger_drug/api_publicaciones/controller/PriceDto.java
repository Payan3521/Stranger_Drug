package com.desarrollox.backend_stranger_drug.api_publicaciones.controller;

import lombok.Data;

@Data
public class PriceDto {
    private String codeCountry;
    private String country;
    private double amount;
    private String currency;
}