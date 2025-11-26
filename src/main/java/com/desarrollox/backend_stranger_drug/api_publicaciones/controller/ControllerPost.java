package com.desarrollox.backend_stranger_drug.api_publicaciones.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend_stranger_drug.api_publicaciones.models.Post;
import com.desarrollox.backend_stranger_drug.api_publicaciones.services.IServicePost;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class ControllerPost {
    private IServicePost servicePost;

    @PostMapping
    public ResponseEntity<Post> savePost(){
        return null;
    }

    @DeleteMapping
    public ResponseEntity<Post> deletePost(){
        return null;
    }

    @PutMapping
    public ResponseEntity<Post> updatePost(){
        return null;
    }

    @GetMapping
    public ResponseEntity<Post> getPost(){
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostByModelName(){
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostBySectionName(){
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostByTitle(){
        return null;
    }
}
