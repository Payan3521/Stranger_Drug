package com.desarrollox.backend_stranger_drug.api_publicaciones.controller;

import java.util.List;
import lombok.Data;

@Data
public class PostDto {
    private String videoUrl;
    private String previewVideoUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private String sectionName;
    private int durationMinutes;
    private List<Long> models;
    private List<PriceDto> prices;
}