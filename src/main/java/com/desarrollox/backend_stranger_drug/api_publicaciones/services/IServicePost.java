package com.desarrollox.backend_stranger_drug.api_publicaciones.services;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.PostDto;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.PostResponseDTO;

public interface IServicePost {
    PostResponseDTO savePost(PostDto postDto);
    Optional<PostResponseDTO> deletePost(Long id);
    Optional<PostResponseDTO> updatePost(Long id, PostDto postDto);
    Optional<PostResponseDTO> getPost(Long id);
    List<PostResponseDTO> getAllPosts();
    List<PostResponseDTO> getPostByModelName(String modelName);
    List<PostResponseDTO> getPostBySectionName(String sectionName);
    List<PostResponseDTO> getPostByTitle(String title);
}