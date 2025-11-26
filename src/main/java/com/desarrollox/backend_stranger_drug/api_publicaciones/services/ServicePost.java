package com.desarrollox.backend_stranger_drug.api_publicaciones.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.desarrollox.backend_stranger_drug.api_videos.models.Photo;
import com.desarrollox.backend_stranger_drug.api_videos.models.Video;
import com.desarrollox.backend_stranger_drug.api_videos.services.IServiceVideo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicePost implements IServicePost {

    private final IRepositoryPost repositoryPost;
    private final IRepositoryModel repositoryModel;
    private final SectionRepository sectionRepository;
    private final IRepositoryPricePost repositoryPricePost;
    private final IRepositoryPostModel repositoryPostModel;
    private final ModelWebMapper modelWebMapper;
    private final SectionWebMapper sectionWebMapper;
    private final PriceWebMapper priceWebMapper;
    private final IServiceVideo serviceVideo;

    @Override
    public PostResponseDTO savePost(PostDto postDto) {
        List<Model> models = repositoryModel.findAllById(postDto.getModels());

        if (models.size() != postDto.getModels().size()) {
            List<Long> encontrados = models.stream()
                    .map(Model::getId)
                    .toList();

            List<Long> faltantes = postDto.getModels().stream()
                    .filter(id -> !encontrados.contains(id))
                    .toList();

            throw new ModelNotFoundException("Las siguientes modelos no se encontraron: " + faltantes);
        }

        if (!sectionRepository.existsByName(postDto.getSectionName())) {
            throw new SectionNotFoundException("La seccion con nombre " + postDto.getSectionName() + " no se encontro");
        }

        Section section = sectionRepository.findByName(postDto.getSectionName());

        Video video = serviceVideo.uploadPreviewVideo(postDto.getVideo());
        Video previewVideo = serviceVideo.uploadPreviewVideo(postDto.getPreviewVideo());
        Photo thumbnail = serviceVideo.uploadThumbnail(postDto.getThumbnail());

        Post post = Post.builder()
                .video(video)
                .previewVideo(previewVideo)
                .thumbnail(thumbnail)
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
        if (repositoryPost.existsById(id)) {
            Optional<Post> post = repositoryPost.findById(id);
            repositoryPost.delete(post.get());
            return Optional.of(new PostResponseDTO());
        } else {
            throw new PostNotFoundException("El post con id " + id + " no se encontro, no se puede eliminar");
        }
    }

    @Override
    public Optional<PostResponseDTO> updatePost(Long id, PostDto postDto) {
        Post post = repositoryPost.findById(id)
                .orElseThrow(() -> new PostNotFoundException("El post con id " + id + " no se encontro"));

        // validar modelos
        List<Model> models = repositoryModel.findAllById(postDto.getModels());

        if (models.size() != postDto.getModels().size()) {
            List<Long> encontrados = models.stream()
                    .map(Model::getId)
                    .toList();

            List<Long> faltantes = postDto.getModels().stream()
                    .filter(modelId -> !encontrados.contains(modelId))
                    .toList();

            throw new ModelNotFoundException("Las siguientes modelos no se encontraron: " + faltantes);
        }

        // validar seccion
        Section section = sectionRepository.findByName(postDto.getSectionName());
        if (section == null) {
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

        // Actualizar modelos
        List<PostModel> actuales = repositoryPostModel.findByPostId(id);
        List<Long> nuevosIds = postDto.getModels();

        // Eliminar los que ya no están
        List<PostModel> aEliminar = actuales.stream()
                .filter(pm -> !nuevosIds.contains(pm.getModel().getId()))
                .toList();
        repositoryPostModel.deleteAll(aEliminar);

        // Agregar los nuevos
        List<Long> idsActuales = actuales.stream()
                .map(pm -> pm.getModel().getId())
                .toList();

        models.stream()
                .filter(m -> !idsActuales.contains(m.getId()))
                .forEach(model -> {
                    PostModel newPm = PostModel.builder()
                            .post(post)
                            .model(model)
                            .build();
                    repositoryPostModel.save(newPm);
                });

        // 5. Actualizar precios
        List<PricePost> preciosActuales = repositoryPricePost.findByPostId(id);

        // Estrategia: Borrar todo y volver a crear es lo que causa el incremento de
        // IDs.
        // Mejor estrategia: Reutilizar entidades existentes si es posible, o
        // simplemente aceptar que los precios son value objects.
        // Dado el requerimiento del usuario de NO incrementar IDs, intentaremos
        // reutilizar.
        // Sin embargo, los precios no tienen un ID natural único aparte del
        // autogenerado.
        // Asumiremos que si el país y moneda coinciden, es el mismo precio y
        // actualizamos el monto.

        // Para simplificar y cumplir con "no incrementar IDs", podemos intentar mapear
        // por algún criterio o simplemente
        // actualizar los primeros N registros y borrar/crear el resto.
        // Pero una mejor aproximación para listas de valores es:
        // 1. Identificar precios por (Country, CodeCountry, Currency) si son únicos.
        // Si no hay clave única lógica, es difícil preservar IDs sin heurísticas.
        // Asumiremos que (CodeCountry, Currency) debería ser único para un post.

        // Mapear precios actuales por una clave compuesta (ej: codeCountry + currency)
        // O simplemente iterar y tratar de hacer match.
        // Dado que el usuario se queja de los IDs, haremos un esfuerzo por preservar.

        // Copia mutable de precios actuales para ir consumiéndolos
        List<PricePost> preciosDisponibles = new ArrayList<>(preciosActuales);

        postDto.getPrices().forEach(dto -> {
            // Buscar si existe un precio con el mismo codeCountry y currency
            Optional<PricePost> existente = preciosDisponibles.stream()
                    .filter(p -> p.getCodeCountry().equals(dto.getCodeCountry())
                            && p.getCurrency().equals(dto.getCurrency()))
                    .findFirst();

            if (existente.isPresent()) {
                PricePost p = existente.get();
                p.setAmount(dto.getAmount());
                p.setCountry(dto.getCountry()); // Actualizar nombre país si cambió
                repositoryPricePost.save(p);
                preciosDisponibles.remove(p); // Ya procesado
            } else {
                // Crear nuevo
                PricePost newPrice = PricePost.builder()
                        .post(post)
                        .codeCountry(dto.getCodeCountry())
                        .country(dto.getCountry())
                        .amount(dto.getAmount())
                        .currency(dto.getCurrency())
                        .build();
                repositoryPricePost.save(newPrice);
            }
        });

        // Eliminar los que sobraron (estaban en BD pero no en el DTO)
        repositoryPricePost.deleteAll(preciosDisponibles);

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

        List<PricePost> precios = repositoryPricePost.findByPostId(post.getId());

        response.setPrices(priceWebMapper.toPriceDtoList(precios));

        return Optional.of(response);
    }

    @Override
    public Optional<PostResponseDTO> getPost(Long id) {
        if (repositoryPost.existsById(id)) {
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

            List<PricePost> precios = repositoryPricePost.findByPostId(id);

            response.setPrices(priceWebMapper.toPriceDtoList(precios));
            return Optional.of(response);
        } else {
            throw new PostNotFoundException("El post con id " + id + " no se encontro");
        }
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = repositoryPost.findAll();
        List<PostResponseDTO> response = new ArrayList<>();

        for (Post post : posts) {
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

            List<PricePost> precios = repositoryPricePost.findByPostId(post.getId());

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

        // Usar un Map para eliminar duplicados basados en el ID del post
        Map<Long, PostResponseDTO> responseMap = new java.util.HashMap<>();

        for (PostModel relacion : relaciones) {
            Post post = relacion.getPost();
            if (responseMap.containsKey(post.getId())) {
                continue;
            }

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

            List<PricePost> precios = repositoryPricePost.findByPostId(post.getId());

            postResponseDTO.setPrices(priceWebMapper.toPriceDtoList(precios));
            responseMap.put(post.getId(), postResponseDTO);
        }
        return new ArrayList<>(responseMap.values());
    }

    @Override
    public List<PostResponseDTO> getPostBySectionName(String sectionName) {
        List<Post> posts = repositoryPost
                .findBySectionNameStartingWithIgnoreCase(sectionName);

        if (posts.isEmpty()) {
            throw new PostNotFoundException("No hay publicaciones en secciones que coincidan con " + sectionName);
        }

        List<PostResponseDTO> response = new ArrayList<>();

        for (Post post : posts) {
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

            List<PricePost> precios = repositoryPricePost.findByPostId(post.getId());

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

        for (Post post : posts) {
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

            List<PricePost> precios = repositoryPricePost.findByPostId(post.getId());

            postResponseDTO.setPrices(priceWebMapper.toPriceDtoList(precios));
            response.add(postResponseDTO);
        }
        return response;
    }
}