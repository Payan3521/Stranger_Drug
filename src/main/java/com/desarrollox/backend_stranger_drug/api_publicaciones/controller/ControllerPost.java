package com.desarrollox.backend_stranger_drug.api_publicaciones.controller;

import java.util.List;
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
import com.desarrollox.backend_stranger_drug.api_publicaciones.models.Post;
import com.desarrollox.backend_stranger_drug.api_publicaciones.services.IServicePost;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class ControllerPost {
    private IServicePost servicePost;

    @PostMapping
    public ResponseEntity<Post> savePost(@Valid @RequestBody PostDto postDto){
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long id){
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @Valid @RequestBody PostDto postDto){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id){
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        return null;
    }

    @GetMapping("/model")
    public ResponseEntity<List<Post>> getPostByModelName(@RequestParam String name){
        return null;
    }

    @GetMapping("/section")
    public ResponseEntity<List<Post>> getPostBySectionName(@RequestParam String name){
        return null;
    }

    @GetMapping("/title")
    public ResponseEntity<List<Post>> getPostByTitle(@RequestParam String title){
        return null;
    }
}
