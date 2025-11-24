package com.desarrollox.backend_stranger_drug.api_secciones.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_secciones.models.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    boolean existsByName(String name);
}
