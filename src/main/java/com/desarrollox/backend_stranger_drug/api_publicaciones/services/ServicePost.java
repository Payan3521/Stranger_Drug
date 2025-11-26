package com.desarrollox.backend_stranger_drug.api_publicaciones.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.desarrollox.backend_stranger_drug.api_modelos.exception.ModelNotFoundException;
import com.desarrollox.backend_stranger_drug.api_modelos.models.Model;
import com.desarrollox.backend_stranger_drug.api_modelos.repositories.IRepositoryModel;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.PostDto;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.PostResponseDTO;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.webMapper.ModelWebMapper;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.webMapper.PriceWebMapper;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.webMapper.SectionWebMapper;
import com.desarrollox.backend_stranger_drug.api_publicaciones.exception.PostNotFoundException;
import com.desarrollox.backend_stranger_drug.api_publicaciones.models.Post;
import com.desarrollox.backend_stranger_drug.api_publicaciones.models.PostModel;
import com.desarrollox.backend_stranger_drug.api_publicaciones.models.PricePost;
import com.desarrollox.backend_stranger_drug.api_publicaciones.repositories.IRepositoryPost;
import com.desarrollox.backend_stranger_drug.api_publicaciones.repositories.IRepositoryPostModel;
import com.desarrollox.backend_stranger_drug.api_publicaciones.repositories.IRepositoryPricePost;
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
    private final IRepositoryPricePost repositoryPricePost;
    private final IRepositoryPostModel repositoryPostModel;
    private final ModelWebMapper modelWebMapper;
    private final SectionWebMapper sectionWebMapper;
    private final PriceWebMapper priceWebMapper;

    @Override
    public PostResponseDTO savePost(PostDto postDto) {
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
            .build();

        repositoryPost.save(post);

        models.forEach(model -> {
            PostModel postModel = PostModel.builder()
                .post(post)
                .model(model)
                .build();

            repositoryPostModel.save(postModel);
        });

        postDto.getPrices().forEach(price -> {
            PricePost pricePost = PricePost.builder()
                .post(post)
                .codeCountry(price.getCodeCountry())
                .country(price.getCountry())
                .amount(price.getAmount())
                .currency(price.getCurrency())
                .build();

            repositoryPricePost.save(pricePost);
        });

        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(post.getId());
        postResponseDTO.setVideoUrl(post.getVideoUrl());
        postResponseDTO.setPreviewUrl(post.getPreviewUrl());
        postResponseDTO.setThumbnailUrl(post.getThumbnailUrl());
        postResponseDTO.setTitle(post.getTitle());
        postResponseDTO.setDescription(post.getDescription());
        postResponseDTO.setSection(sectionWebMapper.toSectionResponseDTO(post.getSection()));
        postResponseDTO.setDurationMinutes(post.getDuration());
        postResponseDTO.setModels(modelWebMapper.toModelDtoList(models));
        postResponseDTO.setPrices(postDto.getPrices());

        return postResponseDTO;
    }

    @Override
    public Optional<PostResponseDTO> deletePost(Long id) {
        if(repositoryPost.existsById(id)){
            Optional<Post> post = repositoryPost.findById(id);
            repositoryPost.delete(post.get());
            return Optional.of(new PostResponseDTO());
        }else{
            throw new PostNotFoundException("El post con id " + id + " no se encontro, no se puede eliminar");
        }
    }

    @Override
    public Optional<PostResponseDTO> updatePost(Long id, PostDto postDto) {
        Post post = repositoryPost.findById(id)
            .orElseThrow(() -> new PostNotFoundException("El post con id " + id + " no se encontro"));

        //validar modelos
        List<Model> models = repositoryModel.findAllById(postDto.getModels());

        if(models.size() != postDto.getModels().size()){
            List<Long> encontrados = models.stream()
                .map(Model::getId)
                .toList();

            List<Long> faltantes = postDto.getModels().stream()
                .filter(modelId -> !encontrados.contains(modelId))
                .toList();

            throw new ModelNotFoundException("Las siguientes modelos no se encontraron: " + faltantes);
        }

        //validar seccion
        Section section = sectionRepository.findByName(postDto.getSectionName());
        if(section == null){
            throw new SectionNotFoundException("La seccion con nombre " + postDto.getSectionName() + " no se encontro");
        }

        post.setSection(section);
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setDuration(postDto.getDurationMinutes());
        post.setVideoUrl(postDto.getVideoUrl());
        post.setPreviewUrl(postDto.getPreviewVideoUrl());
        post.setThumbnailUrl(postDto.getThumbnailUrl());

        repositoryPost.save(post);

        //Actualizar modelos
        List<PostModel> actuales = repositoryPostModel.findAll()
            .stream()
            .filter(pm -> pm.getPost().getId().equals(id))
            .toList();

        repositoryPostModel.deleteAll(actuales);

        models.forEach(model -> {
            PostModel newPm = PostModel.builder()
                    .post(post)
                    .model(model)
                    .build();
            repositoryPostModel.save(newPm);
        });

        // 5. Actualizar precios
        List<PricePost> preciosActuales = repositoryPricePost.findAll()
                .stream()
                .filter(p -> p.getPost().getId().equals(id))
                .toList();

        repositoryPricePost.deleteAll(preciosActuales);

        postDto.getPrices().forEach(price -> {
            PricePost newPrice = PricePost.builder()
                    .post(post)
                    .codeCountry(price.getCodeCountry())
                    .country(price.getCountry())
                    .amount(price.getAmount())
                    .currency(price.getCurrency())
                    .build();

            repositoryPricePost.save(newPrice);
        });

        PostResponseDTO response = new PostResponseDTO();
        response.setId(post.getId());
        response.setVideoUrl(post.getVideoUrl());
        response.setPreviewUrl(post.getPreviewUrl());
        response.setThumbnailUrl(post.getThumbnailUrl());
        response.setTitle(post.getTitle());
        response.setDescription(post.getDescription());
        response.setSection(sectionWebMapper.toSectionResponseDTO(post.getSection()));
        response.setDurationMinutes(post.getDuration());
        
        List<PostModel> relacioness = repositoryPostModel
            .findByPostId(post.getId());
        
        List<Model> modelos = relacioness.stream()
            .map(PostModel::getModel)
            .toList();
        
        response.setModels(modelWebMapper.toModelDtoList(modelos));
        
        List<PricePost> precios = repositoryPricePost.findAll()
            .stream()
            .filter(p -> p.getPost().getId().equals(post.getId()))
            .toList();  
        
        response.setPrices(priceWebMapper.toPriceDtoList(precios));

        return Optional.of(response);
    }

    @Override
    public Optional<PostResponseDTO> getPost(Long id) {
        if(repositoryPost.existsById(id)){
            PostResponseDTO response = new PostResponseDTO();
            response.setId(id);
            response.setVideoUrl(repositoryPost.findById(id).get().getVideoUrl());
            response.setPreviewUrl(repositoryPost.findById(id).get().getPreviewUrl());
            response.setThumbnailUrl(repositoryPost.findById(id).get().getThumbnailUrl());
            response.setTitle(repositoryPost.findById(id).get().getTitle());
            response.setDescription(repositoryPost.findById(id).get().getDescription());
            response.setSection(sectionWebMapper.toSectionResponseDTO(repositoryPost.findById(id).get().getSection()));
            response.setDurationMinutes(repositoryPost.findById(id).get().getDuration());
            
            List<PostModel> relacioness = repositoryPostModel
                .findByPostId(id);
            
            List<Model> modelos = relacioness.stream()
                .map(PostModel::getModel)
                .toList();
            
            response.setModels(modelWebMapper.toModelDtoList(modelos));
            
            List<PricePost> precios = repositoryPricePost.findAll()
                .stream()
                .filter(p -> p.getPost().getId().equals(id))
                .toList();  
            
            response.setPrices(priceWebMapper.toPriceDtoList(precios));
            return Optional.of(response);
        }else{
            throw new PostNotFoundException("El post con id " + id + " no se encontro");
        }
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = repositoryPost.findAll();
        List<PostResponseDTO> response = new ArrayList<>();
        
        for(Post post : posts){
            PostResponseDTO postResponseDTO = new PostResponseDTO();
            postResponseDTO.setId(post.getId());
            postResponseDTO.setVideoUrl(post.getVideoUrl());
            postResponseDTO.setPreviewUrl(post.getPreviewUrl());
            postResponseDTO.setThumbnailUrl(post.getThumbnailUrl());
            postResponseDTO.setTitle(post.getTitle());
            postResponseDTO.setDescription(post.getDescription());
            postResponseDTO.setSection(sectionWebMapper.toSectionResponseDTO(post.getSection()));
            postResponseDTO.setDurationMinutes(post.getDuration());
            
            List<PostModel> relacioness = repositoryPostModel
                .findByPostId(post.getId());
            
            List<Model> modelos = relacioness.stream()
                .map(PostModel::getModel)
                .toList();
            
            postResponseDTO.setModels(modelWebMapper.toModelDtoList(modelos));
            
            List<PricePost> precios = repositoryPricePost.findAll()
                .stream()
                .filter(p -> p.getPost().getId().equals(post.getId()))
                .toList();  
            
            postResponseDTO.setPrices(priceWebMapper.toPriceDtoList(precios));
            response.add(postResponseDTO);
        }
        return response;
    }

    @Override
    public List<PostResponseDTO> getPostByModelName(String modelName) {
        List<PostModel> relaciones = repositoryPostModel
                .findByModelNameContainingIgnoreCase(modelName);

        if (relaciones.isEmpty()) {
            throw new PostNotFoundException("No hay publicaciones con modelos que coincidan con " + modelName);
        }

        List<PostResponseDTO> response = new ArrayList<>();
        
        for(PostModel relacion : relaciones){
            PostResponseDTO postResponseDTO = new PostResponseDTO();
            postResponseDTO.setId(relacion.getPost().getId());
            postResponseDTO.setVideoUrl(relacion.getPost().getVideoUrl());
            postResponseDTO.setPreviewUrl(relacion.getPost().getPreviewUrl());
            postResponseDTO.setThumbnailUrl(relacion.getPost().getThumbnailUrl());
            postResponseDTO.setTitle(relacion.getPost().getTitle());
            postResponseDTO.setDescription(relacion.getPost().getDescription());
            postResponseDTO.setSection(sectionWebMapper.toSectionResponseDTO(relacion.getPost().getSection()));
            postResponseDTO.setDurationMinutes(relacion.getPost().getDuration());
            
            List<PostModel> relacioness = repositoryPostModel
                .findByPostId(relacion.getPost().getId());
            
            List<Model> modelos = relacioness.stream()
                .map(PostModel::getModel)
                .toList();
            
            postResponseDTO.setModels(modelWebMapper.toModelDtoList(modelos));
            
            List<PricePost> precios = repositoryPricePost.findAll()
                .stream()
                .filter(p -> p.getPost().getId().equals(relacion.getPost().getId()))
                .toList();  
            
            postResponseDTO.setPrices(priceWebMapper.toPriceDtoList(precios));
            response.add(postResponseDTO);
        }
        return response;
    }

    @Override
    public List<PostResponseDTO> getPostBySectionName(String sectionName) {
        List<Post> posts = repositoryPost
                .findBySectionNameStartingWithIgnoreCase(sectionName);

        if (posts.isEmpty()) {
            throw new PostNotFoundException("No hay publicaciones en secciones que coincidan con " + sectionName);
        }

        List<PostResponseDTO> response = new ArrayList<>();
        
        for(Post post : posts){
            PostResponseDTO postResponseDTO = new PostResponseDTO();
            postResponseDTO.setId(post.getId());
            postResponseDTO.setVideoUrl(post.getVideoUrl());
            postResponseDTO.setPreviewUrl(post.getPreviewUrl());
            postResponseDTO.setThumbnailUrl(post.getThumbnailUrl());
            postResponseDTO.setTitle(post.getTitle());
            postResponseDTO.setDescription(post.getDescription());
            postResponseDTO.setSection(sectionWebMapper.toSectionResponseDTO(post.getSection()));
            postResponseDTO.setDurationMinutes(post.getDuration());
            
            List<PostModel> relaciones = repositoryPostModel
                .findByPostId(post.getId());
            
            List<Model> modelos = relaciones.stream()
                .map(PostModel::getModel)
                .toList();
            
            postResponseDTO.setModels(modelWebMapper.toModelDtoList(modelos));
            
            List<PricePost> precios = repositoryPricePost.findAll()
                .stream()
                .filter(p -> p.getPost().getId().equals(post.getId()))
                .toList();  
            
            postResponseDTO.setPrices(priceWebMapper.toPriceDtoList(precios));
            response.add(postResponseDTO);
        }
        return response;
    }

    @Override
    public List<PostResponseDTO> getPostByTitle(String title) {
        List<Post> posts = repositoryPost
                .findByTitleContainingIgnoreCase(title);

        if (posts.isEmpty()) {
            throw new PostNotFoundException("No hay publicaciones cuyo título coincida con " + title);
        }

        List<PostResponseDTO> response = new ArrayList<>();
        
        for(Post post : posts){
            PostResponseDTO postResponseDTO = new PostResponseDTO();
            postResponseDTO.setId(post.getId());
            postResponseDTO.setVideoUrl(post.getVideoUrl());
            postResponseDTO.setPreviewUrl(post.getPreviewUrl());
            postResponseDTO.setThumbnailUrl(post.getThumbnailUrl());
            postResponseDTO.setTitle(post.getTitle());
            postResponseDTO.setDescription(post.getDescription());
            postResponseDTO.setSection(sectionWebMapper.toSectionResponseDTO(post.getSection()));
            postResponseDTO.setDurationMinutes(post.getDuration());
            
            List<PostModel> relaciones = repositoryPostModel
                .findByPostId(post.getId());
            
            List<Model> modelos = relaciones.stream()
                .map(PostModel::getModel)
                .toList();
            
            postResponseDTO.setModels(modelWebMapper.toModelDtoList(modelos));
            
            List<PricePost> precios = repositoryPricePost.findAll()
                .stream()
                .filter(p -> p.getPost().getId().equals(post.getId()))
                .toList();  
            
            postResponseDTO.setPrices(priceWebMapper.toPriceDtoList(precios));
            response.add(postResponseDTO);
        }
        return response;
    }
}