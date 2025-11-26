package com.desarrollox.backend_stranger_drug.api_publicaciones.controller.webMapper;

import org.springframework.stereotype.Component;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.dto.SectionResponseDto;
import com.desarrollox.backend_stranger_drug.api_secciones.models.Section;

@Component
public class SectionWebMapper {
    public SectionResponseDto toSectionResponseDTO(Section section){
        SectionResponseDto sectionResponseDto = new SectionResponseDto();
        sectionResponseDto.setId(section.getId());
        sectionResponseDto.setName(section.getName());
        sectionResponseDto.setDescription(section.getDescription());
        return sectionResponseDto;
    }
}
