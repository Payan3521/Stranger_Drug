package com.desarrollox.backend_stranger_drug.api_publicaciones.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Price {
    private Long id;
    private Post post;
    private String codeCountry;
    private String country;
    private double amount;
    private String currency;
}