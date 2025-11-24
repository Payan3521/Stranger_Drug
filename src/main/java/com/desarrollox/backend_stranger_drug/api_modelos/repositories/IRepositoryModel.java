package com.desarrollox.backend_stranger_drug.api_modelos.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_modelos.models.Model;

@Repository
public interface IRepositoryModel extends JpaRepository<Model, Long>{
    List<Model> findByName(String name);
}
