package com.desarrollox.backend_stranger_drug.api_videos.services;

import org.springframework.stereotype.Service;
import com.desarrollox.backend_stranger_drug.api_videos.repositories.IRepositoryVideo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceVideo implements IServiceVideo {
    private final IRepositoryVideo repositoryVideo;
}
