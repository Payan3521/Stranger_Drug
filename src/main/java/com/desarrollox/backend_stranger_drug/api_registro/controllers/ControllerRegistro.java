package com.desarrollox.backend_stranger_drug.api_registro.controllers;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;
import com.desarrollox.backend_stranger_drug.api_registro.services.IServiceRegistro;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class ControllerRegistro {

    private final IServiceRegistro serviceRegistro;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user){
        User saved = serviceRegistro.create(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        Optional<User> user = serviceRegistro.findById(id);
        return user.map(value-> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<User> findByEmail(@RequestParam String email){
        Optional<User> user = serviceRegistro.findByEmail(email);
        return user.map(value-> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
}