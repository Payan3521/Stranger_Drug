package com.desarrollox.backend_stranger_drug.api_publicaciones.models;

import com.desarrollox.backend_stranger_drug.api_modelos.models.Model;
import lombok.Data;

@Data
public class PostModel {
    private Long id;
    private Post post;
    private Model model;
}