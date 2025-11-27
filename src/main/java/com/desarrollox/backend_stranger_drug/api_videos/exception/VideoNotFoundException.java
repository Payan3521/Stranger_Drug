package com.desarrollox.backend_stranger_drug.api_videos.exception;

public class VideoNotFoundException extends RuntimeException{
    
    public VideoNotFoundException(String message){
        super(message);
    }
}