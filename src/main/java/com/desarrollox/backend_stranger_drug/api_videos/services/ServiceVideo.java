package com.desarrollox.backend_stranger_drug.api_videos.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.desarrollox.backend_stranger_drug.api_videos.models.Photo;
import com.desarrollox.backend_stranger_drug.api_videos.models.Video;
import com.desarrollox.backend_stranger_drug.api_videos.repositories.IRepositoryVideo;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@RequiredArgsConstructor
public class ServiceVideo implements IServiceVideo {

    private final IRepositoryVideo repositoryVideo;
    private final S3Client s3Client;

    @Override
    public Video uploadVideo(MultipartFile file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadVideo'");
    }

    @Override
    public Video uploadPreviewVideo(MultipartFile file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadPreviewVideo'");
    }

    @Override
    public Photo uploadThumbnail(MultipartFile file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadThumbnail'");
    }

    @Override
    public Optional<Video> findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<Video> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Video> updateVideo(Long id, Video video) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateVideo'");
    }

    @Override
    public Optional<Video> deleteVideo(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteVideo'");
    }

    private String subirArchivo(MultipartFile file, String nombre){
        return null;
    }

    private void eliminarArchivo(){

    }
}