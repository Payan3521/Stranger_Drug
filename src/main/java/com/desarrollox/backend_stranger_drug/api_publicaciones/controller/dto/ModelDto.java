package com.desarrollox.backend_stranger_drug.api_publicaciones.controller.dto;

import lombok.Data;

@Data
public class ModelDto {
    private Long id;
    private String name;
    private String photoUrl;
    private String biography;
}