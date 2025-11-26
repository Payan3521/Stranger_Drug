package com.desarrollox.backend_stranger_drug.api_publicaciones.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.desarrollox.backend_stranger_drug.api_modelos.exception.ModelNotFoundException;
import com.desarrollox.backend_stranger_drug.api_modelos.models.Model;
import com.desarrollox.backend_stranger_drug.api_modelos.repositories.IRepositoryModel;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.PostDto;
import com.desarrollox.backend_stranger_drug.api_publicaciones.models.Post;
import com.desarrollox.backend_stranger_drug.api_publicaciones.repositories.IRepositoryPost;
import com.desarrollox.backend_stranger_drug.api_publicaciones.repositories.IRepositoryPostModel;
import com.desarrollox.backend_stranger_drug.api_publicaciones.repositories.IRepositoryPrice;
import com.desarrollox.backend_stranger_drug.api_secciones.exception.SectionNotFoundException;
import com.desarrollox.backend_stranger_drug.api_secciones.models.Section;
import com.desarrollox.backend_stranger_drug.api_secciones.repositories.SectionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicePost implements IServicePost{

    private final IRepositoryPost repositoryPost;
    private final IRepositoryModel repositoryModel;
    private final SectionRepository sectionRepository;
    private final IRepositoryPrice repositoryPrice;
    private final IRepositoryPostModel repositoryPostModel;

    @Override
    public Post savePost(PostDto postDto) {
        List<Model> models = repositoryModel.findAllById(postDto.getModels());

        if(models.size() != postDto.getModels().size()){
            List<Long> encontrados = models.stream()
                .map(Model::getId)
                .toList();

            List<Long> faltantes = postDto.getModels().stream()
                .filter(id -> !encontrados.contains(id))
                .toList();

            throw new ModelNotFoundException("Las siguientes modelos no se encontraron: " + faltantes);
        }

        if(!sectionRepository.existsByName(postDto.getSectionName())){
            throw new SectionNotFoundException("La seccion con nombre " + postDto.getSectionName() + " no se encontro");
        }

        Section section = sectionRepository.findByName(postDto.getSectionName());

        Post post = Post.builder()
            .videoUrl(postDto.getVideoUrl())
            .previewUrl(postDto.getPreviewVideoUrl())
            .thumbnailUrl(postDto.getThumbnailUrl())
            .title(postDto.getTitle())
            .description(postDto.getDescription())
            .section(section)
            .duration(postDto.getDurationMinutes())
            .models(models)
            .build();

        repositoryPost.save(post);
    }

    @Override
    public Optional<Post> deletePost(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePost'");
    }

    @Override
    public Optional<Post> updatePost(Long id, PostDto postDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePost'");
    }

    @Override
    public Optional<Post> getPost(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPost'");
    }

    @Override
    public List<Post> getAllPosts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPosts'");
    }

    @Override
    public List<Post> getPostByModelName(String modelName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPostByModelName'");
    }

    @Override
    public List<Post> getPostBySectionName(String sectionName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPostBySectionName'");
    }

    @Override
    public List<Post> getPostByTitle(String title) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPostByTitle'");
    }
}