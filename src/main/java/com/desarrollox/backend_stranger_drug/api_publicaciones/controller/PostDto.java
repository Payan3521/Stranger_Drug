package com.desarrollox.backend_stranger_drug.api_publicaciones.controller;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class PostDto {
    private MultipartFile video;
    private MultipartFile previewVideo;
    private MultipartFile thumbnail;
    private String title;
    private String description;
    private String sectionName;
    private int durationMinutes;
    private List<Long> models;
    private List<PriceDto> prices;
}