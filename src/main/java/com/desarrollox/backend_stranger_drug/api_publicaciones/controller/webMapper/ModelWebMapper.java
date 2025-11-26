package com.desarrollox.backend_stranger_drug.api_publicaciones.controller.webMapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.desarrollox.backend_stranger_drug.api_modelos.models.Model;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.dto.ModelDto;

@Component
public class ModelWebMapper {

    public List<ModelDto> toModelDtoList(List<Model> models) {
        return models.stream()
            .map(model -> {
                ModelDto modelDto = new ModelDto();
                modelDto.setId(model.getId());
                modelDto.setName(model.getName());
                modelDto.setPhotoUrl(model.getPhotoUrl());
                modelDto.setBiography(model.getBiography());
                return modelDto;
            })
            .toList();
    }
    
}