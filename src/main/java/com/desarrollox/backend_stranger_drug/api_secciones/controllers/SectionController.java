package com.desarrollox.backend_stranger_drug.api_secciones.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend_stranger_drug.api_secciones.models.Section;
import com.desarrollox.backend_stranger_drug.api_secciones.services.ISectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sections")
public class SectionController {

    private final ISectionService sectionService;

    @PostMapping
    public ResponseEntity<Section> create(@Valid @RequestBody Section section) {
        Section saved = sectionService.create(section);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Section>> findAll() {
        List<Section> sections = sectionService.findAll();
        if (sections.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Section> findById(@PathVariable Long id) {
        Optional<Section> section = sectionService.findById(id);
        return new ResponseEntity<>(section.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sectionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
