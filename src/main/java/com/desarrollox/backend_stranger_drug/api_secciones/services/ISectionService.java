package com.desarrollox.backend_stranger_drug.api_secciones.services;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend_stranger_drug.api_secciones.models.Section;

public interface ISectionService {

    Section create(Section section);

    List<Section> findAll();

    Optional<Section> findById(Long id);

    Optional<Section> delete(Long id);
}
