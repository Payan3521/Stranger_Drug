package com.desarrollox.backend_stranger_drug.api_publicaciones.controller;

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

import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.dto.PostDto;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.dto.PostResponseDTO;
import com.desarrollox.backend_stranger_drug.api_publicaciones.services.IServicePost;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class ControllerPost {
    private final IServicePost servicePost;

    @PostMapping
    public ResponseEntity<PostResponseDTO> savePost(@Valid @RequestBody PostDto postDto){
        PostResponseDTO postResponseDTO = servicePost.savePost(postDto);
        return new ResponseEntity<>(postResponseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        servicePost.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, @Valid @RequestBody PostDto postDto){
        Optional<PostResponseDTO> post = servicePost.updatePost(id, postDto);
        return new ResponseEntity<>(post.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id){
        Optional<PostResponseDTO> post = servicePost.getPost(id);
        return new ResponseEntity<>(post.get(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts(){
        List<PostResponseDTO> posts = servicePost.getAllPosts();
        if(posts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/model")
    public ResponseEntity<List<PostResponseDTO>> getPostByModelName(@RequestParam String name){
        List<PostResponseDTO> posts = servicePost.getPostByModelName(name);
        if(posts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/section")
    public ResponseEntity<List<PostResponseDTO>> getPostBySectionName(@RequestParam String name){
        List<PostResponseDTO> posts = servicePost.getPostBySectionName(name);
        if(posts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<List<PostResponseDTO>> getPostByTitle(@RequestParam String title){
        List<PostResponseDTO> posts = servicePost.getPostByTitle(title);
        if(posts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
