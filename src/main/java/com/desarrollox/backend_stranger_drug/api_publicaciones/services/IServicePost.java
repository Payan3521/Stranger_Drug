package com.desarrollox.backend_stranger_drug.api_publicaciones.services;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.PostDto;
import com.desarrollox.backend_stranger_drug.api_publicaciones.models.Post;

public interface IServicePost {
    Post savePost(PostDto postDto);
    Optional<Post> deletePost(Long id);
    Optional<Post> updatePost(Long id, PostDto postDto);
    Optional<Post> getPost(Long id);
    List<Post> getAllPosts();
    List<Post> getPostByModelName(String modelName);
    List<Post> getPostBySectionName(String sectionName);
    List<Post> getPostByTitle(String title);
}