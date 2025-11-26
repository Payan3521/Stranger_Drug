package com.desarrollox.backend_stranger_drug.api_videos.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.desarrollox.backend_stranger_drug.api_videos.models.Photo;
import com.desarrollox.backend_stranger_drug.api_videos.models.Video;
import com.desarrollox.backend_stranger_drug.api_videos.services.IServiceVideo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class ControllerVideo {
    private final IServiceVideo serviceVideo;

    @PostMapping("/upload-video")
    public ResponseEntity<Video> uploadVideo(@RequestParam("video") MultipartFile file){
        Video video = serviceVideo.uploadVideo(file);
        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @PostMapping("/upload-preview-video")
    public ResponseEntity<Video> uploadPreviewVideo(@RequestParam("previewVideo") MultipartFile file){
        Video video = serviceVideo.uploadPreviewVideo(file);
        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @PostMapping("/upload-thumbnail")
    public ResponseEntity<Photo> uploadThumbnail(@RequestParam("thumbnail") MultipartFile file){
        Photo photo = serviceVideo.uploadThumbnail(file);
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Video> findById(@PathVariable Long id){
        Optional<Video> video = serviceVideo.findById(id);
        return new ResponseEntity<>(video.get(), HttpStatus.OK);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<Video>> findAll(){
        List<Video> videos = serviceVideo.findAll();
        if(videos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @PutMapping("/update-video/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable Long id, @RequestBody Video video){
        Optional<Video> videoUpdated = serviceVideo.updateVideo(id, video);
        return new ResponseEntity<>(videoUpdated.get(), HttpStatus.OK);
    }

    @DeleteMapping("/delete-video/{id}")
    public ResponseEntity<Video> deleteVideo(@PathVariable Long id){
        Optional<Video> videoDeleted = serviceVideo.deleteVideo(id);
        return new ResponseEntity<>(videoDeleted.get(), HttpStatus.OK);
    }

    @GetMapping("/stream")
    public ResponseEntity<String> getVideoStream(@RequestParam("idUsuario") Long idUsuario, @RequestParam("idVideo") Long idVideo){
        String presignedUrl = serviceVideo.getPresignedUrl()
    }
}
