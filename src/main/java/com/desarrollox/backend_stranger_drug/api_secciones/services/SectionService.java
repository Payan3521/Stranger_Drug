package com.desarrollox.backend_stranger_drug.api_secciones.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.desarrollox.backend_stranger_drug.api_secciones.exception.SectionAlreadyExistsException;
import com.desarrollox.backend_stranger_drug.api_secciones.exception.SectionNotFoundException;
import com.desarrollox.backend_stranger_drug.api_secciones.models.Section;
import com.desarrollox.backend_stranger_drug.api_secciones.repositories.SectionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService implements ISectionService {

    private final SectionRepository sectionRepository;

    @Override
    public Section create(Section section) {
        if (sectionRepository.existsByName(section.getName())) {
            throw new SectionAlreadyExistsException("La sección con el nombre '" + section.getName() + "' ya existe.");
        }
        return sectionRepository.save(section);
    }

    @Override
    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    @Override
    public Optional<Section> findById(Long id) {
        if (!sectionRepository.existsById(id)) {
            throw new SectionNotFoundException("La sección con el id '" + id + "' no existe.");
        }
        return sectionRepository.findById(id);
    }

    @Override
    public Optional<Section> delete(Long id) {
        Optional<Section> section = sectionRepository.findById(id);
        if (section.isPresent()) {
            sectionRepository.delete(section.get());
            return section;
        }else{
            throw new SectionNotFoundException("La sección con el id '" + id + "' no existe.");
        }
    }
}
