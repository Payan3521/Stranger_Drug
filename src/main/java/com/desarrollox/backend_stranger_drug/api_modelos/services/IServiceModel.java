package com.desarrollox.backend_stranger_drug.api_modelos.services;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend_stranger_drug.api_modelos.models.Model;

public interface IServiceModel {
    Model create(Model model);
    Optional<Model> delete(Long id);
    Optional<Model> update(Long id, Model model);
    Optional<Model> findById(Long id);
    List<Model> findByName(String name);
}
