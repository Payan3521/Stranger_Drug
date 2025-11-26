package com.desarrollox.backend_stranger_drug.api_videos.services;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend_stranger_drug.api_videos.models.Video;
import com.desarrollox.backend_stranger_drug.api_videos.models.Photo;
import org.springframework.web.multipart.MultipartFile;

public interface IServiceVideo {
    Video uploadVideo(MultipartFile file);

    Video uploadPreviewVideo(MultipartFile file);

    Photo uploadThumbnail(MultipartFile file);

    Optional<Video> findById(Long id);

    List<Video> findAll();

    Optional<Video> updateVideo(Long id, Video video);

    Optional<Video> deleteVideo(Long id);

    String getPresignedUrl(String key);
}