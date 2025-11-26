package com.desarrollox.backend_stranger_drug.api_videos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend_stranger_drug.api_videos.services.IServiceVideo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class ControllerVideo {
    private final IServiceVideo serviceVideo;
}
