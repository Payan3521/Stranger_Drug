package com.desarrollox.backend_stranger_drug.api_registro.services;

import java.util.Optional;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;

public interface IServiceRegistro {
    User create(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}