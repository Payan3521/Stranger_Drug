package com.desarrollox.backend_stranger_drug.api_modelos.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.desarrollox.backend_stranger_drug.api_modelos.exception.ModelNotFoundException;
import com.desarrollox.backend_stranger_drug.api_modelos.models.Model;
import com.desarrollox.backend_stranger_drug.api_modelos.repositories.IRepositoryModel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceModel implements IServiceModel{
    private final IRepositoryModel repositoryModel;

    @Override
    public Model create(Model model) {
        Model saved = repositoryModel.save(model);
        return saved;
    }

    @Override
    public Optional<Model> delete(Long id) {
        if(repositoryModel.existsById(id)){
            Optional<Model> model = repositoryModel.findById(id);
            repositoryModel.delete(model.get());
            return Optional.of(model.get());
        }else{
            throw new ModelNotFoundException("El/La modelo con id: " + id + " no fue encontrad@, por lo que no se pudo eliminar");
        }
    }

    @Override
    public Optional<Model> update(Long id, Model model) {
        if (repositoryModel.existsById(id)) {
            Model updated = repositoryModel.findById(id).get();

            updated.setName(model.getName());
            updated.setBiography(model.getBiography());
            updated.setPhotoUrl(model.getPhotoUrl());

            Model saved = repositoryModel.save(updated);
            return Optional.of(saved);
        }else{
            throw new ModelNotFoundException("El/La modelo con id: "+ id + " No fue encontrad@, por lo que no se pudo modificar");
        }
    }

    @Override
    public Optional<Model> findById(Long id) {
        if(repositoryModel.existsById(id)){
            return Optional.of(repositoryModel.findById(id).get());
        }else{
            throw new ModelNotFoundException("El/La modelo con id: " + id + " No fue encontrad@");
        }
    }

    @Override
    public List<Model> findByName(String name) {
        return repositoryModel.findByName(name);
    }
}
