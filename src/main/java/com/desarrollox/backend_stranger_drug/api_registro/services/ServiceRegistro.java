package com.desarrollox.backend_stranger_drug.api_registro.services;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.desarrollox.backend_stranger_drug.api_registro.exception.UserAlreadyRegisteredException;
import com.desarrollox.backend_stranger_drug.api_registro.exception.UserNotFoundException;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;
import com.desarrollox.backend_stranger_drug.api_registro.models.User.Role;
import com.desarrollox.backend_stranger_drug.api_registro.repositories.IRepositoryRegistro;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceRegistro implements IServiceRegistro{
    
    private final IRepositoryRegistro repositoryRegistro;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        if(repositoryRegistro.findByEmail(user.getEmail()).isPresent()){
            throw new UserAlreadyRegisteredException("El usuario con email: " + user.getEmail() + " Ya está registrado");
        }

        user.setRole(Role.CLIENTE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User respuesta = repositoryRegistro.save(user);
        return respuesta;
    }

    @Override
    public Optional<User> findById(Long id) {
        if(repositoryRegistro.existsById(id)){
            return Optional.of(repositoryRegistro.findById(id).get());
        }else{
            throw new UserNotFoundException("El usuario con id: " + id + " No fue encontrado");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if(repositoryRegistro.findByEmail(email).isPresent()){
            return Optional.of(repositoryRegistro.findByEmail(email).get());
        }else{
            throw new UserNotFoundException("El usuario con email: " + email + " No fue encontrado");
        }
    }
    
}