package com.desarrollox.backend_stranger_drug.api_videos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_videos.models.Video;

@Repository
public interface IRepositoryVideo extends JpaRepository<Video, Long> {
    
}
