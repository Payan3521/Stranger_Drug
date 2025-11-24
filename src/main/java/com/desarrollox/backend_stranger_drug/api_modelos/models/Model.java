package com.desarrollox.backend_stranger_drug.api_modelos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "models")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 255, message = "El nombre debe tener entre 2 y 255 caracteres")
    private String name;

    @Column(name = "biography", nullable = false)
    @NotBlank(message = "La biografia es obligatoria")
    @Size(min = 2, max = 255, message = "La biografia debe tener entre 2 y 255 caracteres")
    private String biography;

    @Column(name = "photo_url", nullable = false)
    @NotBlank(message = "La url de la foto es obligatoria")
    @Size(min = 2, max = 255, message = "La url de la foto debe tener entre 2 y 255 caracteres")
    @URL(message = "La url de la foto debe ser una url valida")
    private String photoUrl;
}