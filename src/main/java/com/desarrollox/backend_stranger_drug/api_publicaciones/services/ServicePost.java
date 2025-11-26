package com.desarrollox.backend_stranger_drug.api_publicaciones.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.desarrollox.backend_stranger_drug.api_compras.repositories.IRepositoryPurchase;
import com.desarrollox.backend_stranger_drug.api_modelos.exception.ModelNotFoundException;
import com.desarrollox.backend_stranger_drug.api_modelos.models.Model;
import com.desarrollox.backend_stranger_drug.api_modelos.repositories.IRepositoryModel;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.dto.PostDto;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.dto.PostResponseDTO;
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
import com.desarrollox.backend_stranger_drug.api_registro.models.User;
import com.desarrollox.backend_stranger_drug.api_registro.repositories.IRepositoryRegistro;
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
    private final IRepositoryPurchase repositoryPurchase;
    private final IRepositoryRegistro repositoryRegistro;

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

        Video video = serviceVideo.uploadVideo(postDto.getVideo());
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

        if (postDto.getPrices() != null) {
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
        }

        return mapToResponseDTO(post);
    }

    @Override
    public Optional<PostResponseDTO> deletePost(Long id) {
        if (repositoryPost.existsById(id)) {
            Optional<Post> post = repositoryPost.findById(id);
            // Optionally delete videos from S3 here if needed
            // serviceVideo.deleteVideo(post.get().getVideo().getId());
            // serviceVideo.deleteVideo(post.get().getPreviewVideo().getId());
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

        // Handle file updates if provided
        if (postDto.getVideo() != null && !postDto.getVideo().isEmpty()) {
            Video newVideo = serviceVideo.uploadVideo(postDto.getVideo());
            post.setVideo(newVideo);
        }
        if (postDto.getPreviewVideo() != null && !postDto.getPreviewVideo().isEmpty()) {
            Video newPreview = serviceVideo.uploadPreviewVideo(postDto.getPreviewVideo());
            post.setPreviewVideo(newPreview);
        }
        if (postDto.getThumbnail() != null && !postDto.getThumbnail().isEmpty()) {
            Photo newThumbnail = serviceVideo.uploadThumbnail(postDto.getThumbnail());
            post.setThumbnail(newThumbnail);
        }

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

        // Actualizar precios
        List<PricePost> preciosActuales = repositoryPricePost.findByPostId(id);
        List<PricePost> preciosDisponibles = new ArrayList<>(preciosActuales);

        if (postDto.getPrices() != null) {
            postDto.getPrices().forEach(dto -> {
                Optional<PricePost> existente = preciosDisponibles.stream()
                        .filter(p -> p.getCodeCountry().equals(dto.getCodeCountry())
                                && p.getCurrency().equals(dto.getCurrency()))
                        .findFirst();

                if (existente.isPresent()) {
                    PricePost p = existente.get();
                    p.setAmount(dto.getAmount());
                    p.setCountry(dto.getCountry());
                    repositoryPricePost.save(p);
                    preciosDisponibles.remove(p);
                } else {
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
        }

        repositoryPricePost.deleteAll(preciosDisponibles);

        return Optional.of(mapToResponseDTO(post));
    }

    @Override
    public Optional<PostResponseDTO> getPost(Long id) {
        return repositoryPost.findById(id).map(this::mapToResponseDTO);
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        return repositoryPost.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public List<PostResponseDTO> getPostByModelName(String modelName) {
        List<PostModel> relaciones = repositoryPostModel
                .findByModelNameContainingIgnoreCase(modelName);

        if (relaciones.isEmpty()) {
            throw new PostNotFoundException("No hay publicaciones con modelos que coincidan con " + modelName);
        }

        Map<Long, PostResponseDTO> responseMap = new java.util.HashMap<>();

        for (PostModel relacion : relaciones) {
            Post post = relacion.getPost();
            if (!responseMap.containsKey(post.getId())) {
                responseMap.put(post.getId(), mapToResponseDTO(post));
            }
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

        return posts.stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public List<PostResponseDTO> getPostByTitle(String title) {
        List<Post> posts = repositoryPost
                .findByTitleContainingIgnoreCase(title);

        if (posts.isEmpty()) {
            throw new PostNotFoundException("No hay publicaciones cuyo título coincida con " + title);
        }

        return posts.stream().map(this::mapToResponseDTO).toList();
    }

    private PostResponseDTO mapToResponseDTO(Post post) {
        PostResponseDTO response = new PostResponseDTO();
        response.setId(post.getId());

        // Access Control Logic
        String mainVideoUrl = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            Optional<User> user = repositoryRegistro.findByEmail(email);
            if (user.isPresent()) {
                // Check if user purchased the video
                boolean purchased = repositoryPurchase.existsByBuyerUserIdAndVideoUrlId(user.get().getId(),
                        post.getVideo().getId());
                // Also allow if user is the creator? Or ADMIN? Assuming ADMIN can see all.
                // For now, just purchase check.
                if (purchased) {
                    mainVideoUrl = serviceVideo.getPresignedUrl(post.getVideo().getS3Key());
                }
            }
        }

        response.setVideoUrl(mainVideoUrl);

        // Preview is always accessible
        response.setPreviewUrl(serviceVideo.getPresignedUrl(post.getPreviewVideo().getS3Key()));

        // Thumbnail is always accessible
        response.setThumbnailUrl(post.getThumbnail().getS3Url());

        response.setTitle(post.getTitle());
        response.setDescription(post.getDescription());
        response.setSection(sectionWebMapper.toSectionResponseDTO(post.getSection()));
        response.setDurationMinutes(post.getDuration());

        List<PostModel> relacioness = repositoryPostModel.findByPostId(post.getId());
        List<Model> modelos = relacioness.stream()
                .map(PostModel::getModel)
                .toList();
        response.setModels(modelWebMapper.toModelDtoList(modelos));

        List<PricePost> precios = repositoryPricePost.findByPostId(post.getId());
        response.setPrices(priceWebMapper.toPriceDtoList(precios));

        return response;
    }
}