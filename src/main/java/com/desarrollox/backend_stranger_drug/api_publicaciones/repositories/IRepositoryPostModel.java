package com.desarrollox.backend_stranger_drug.api_publicaciones.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_publicaciones.models.PostModel;

@Repository
public interface IRepositoryPostModel extends JpaRepository<PostModel, Long> {
    List<PostModel> findByModelNameContainingIgnoreCase(String name);
    List<PostModel> findByPostId(Long postId);
}