package com.desarrollox.backend_stranger_drug.api_publicaciones.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_publicaciones.models.Post;

@Repository
public interface IRepositoryPost extends JpaRepository<Post, Long> {
    
}