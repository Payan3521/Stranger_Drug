package com.desarrollox.backend_stranger_drug.api_publicaciones.controller;

import java.util.List;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.dto.SectionResponseDto;
import lombok.Data;

@Data
public class PostResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private String previewUrl;
    private String thumbnailUrl;
    private SectionResponseDto section;
    private int durationMinutes;
    private List<ModelDto> models;
    private List<PriceDto> prices;
}