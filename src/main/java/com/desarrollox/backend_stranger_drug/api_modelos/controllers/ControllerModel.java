package com.desarrollox.backend_stranger_drug.api_modelos.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend_stranger_drug.api_modelos.models.Model;
import com.desarrollox.backend_stranger_drug.api_modelos.services.IServiceModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/models")
public class ControllerModel {

    private final IServiceModel serviceModel;

    @PostMapping
    public ResponseEntity<Model> create(@Valid @RequestBody Model model){
        Model saved = serviceModel.create(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Model> delete(@PathVariable Long id){
        serviceModel.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Model> update(@PathVariable Long id, @RequestBody Model model){
        Optional<Model> updated = serviceModel.update(id, model);
        return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> findById(@PathVariable Long id){
        Optional<Model> model = serviceModel.findById(id);
        return new ResponseEntity<>(model.get(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Model>> findByName(@RequestParam String name){
        List<Model> modelList = serviceModel.findByName(name);
        if(modelList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(modelList, HttpStatus.OK);
    }
}
