package com.desarrollox.backend_stranger_drug.api_videos.services;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.desarrollox.backend_stranger_drug.api_videos.models.Photo;
import com.desarrollox.backend_stranger_drug.api_videos.models.Video;
import com.desarrollox.backend_stranger_drug.api_videos.repositories.IRepositoryPhoto;
import com.desarrollox.backend_stranger_drug.api_videos.repositories.IRepositoryVideo;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class ServiceVideo implements IServiceVideo {

    private final IRepositoryVideo repositoryVideo;
    private final IRepositoryPhoto repositoryPhoto;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public Video uploadVideo(MultipartFile file) {
        String key = uploadToS3(file, "videos/principales");
        Video video = Video.builder()
                .s3Bucket(bucketName)
                .s3Key(key)
                .type(Video.VideoType.MAIN)
                .build();
        return repositoryVideo.save(video);
    }

    @Override
    public Video uploadPreviewVideo(MultipartFile file) {
        String key = uploadToS3(file, "videos/previews");
        String url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
        
        Video video = Video.builder()
                .s3Bucket(bucketName)
                .s3Key(key)
                .s3Url(url)
                .type(Video.VideoType.PREVIEW)
                .build();
        return repositoryVideo.save(video);
    }

    @Override
    public Photo uploadThumbnail(MultipartFile file) {
        String key = uploadToS3(file, "miniaturas");
        // For photos, we might want to generate a public URL or just store the key
        // The Photo entity has s3Url, let's generate a presigned one or a direct one if
        // public
        // Assuming private bucket, we store the key. The s3Url field in Photo might be
        // redundant if we use presigned URLs,
        // or it could be the permanent S3 URL (which is not accessible if private).
        // Let's store the standard S3 URL format.
        String url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);

        Photo photo = new Photo();
        photo.setS3Bucket(bucketName);
        photo.setS3Key(key);
        photo.setS3Url(url);
        return repositoryPhoto.save(photo);
    }

    @Override
    public Optional<Video> findById(Long id) {
        return repositoryVideo.findById(id);
    }

    @Override
    public List<Video> findAll() {
        return repositoryVideo.findAll();
    }

    @Override
    public Optional<Video> updateVideo(Long id, Video video) {
        if (repositoryVideo.existsById(id)) {
            video.setId(id);
            return Optional.of(repositoryVideo.save(video));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Video> deleteVideo(Long id) {
        Optional<Video> video = repositoryVideo.findById(id);
        if (video.isPresent()) {
            // Delete from S3
            deleteFromS3(video.get().getS3Key());
            repositoryVideo.deleteById(id);
            return video;
        }
        return Optional.empty();
    }

    @Override
    public String getPresignedUrl(String key) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // 1 hour validity
                .getObjectRequest(b -> b.bucket(bucketName).key(key))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    private String uploadToS3(MultipartFile file, String folder) {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String key = folder + "/" + filename;

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return key;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

    private void deleteFromS3(String key) {
        s3Client.deleteObject(b -> b.bucket(bucketName).key(key));
    }
}