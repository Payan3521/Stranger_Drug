package com.desarrollox.backend_stranger_drug.api_registro.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;

@Repository
public interface IRepositoryRegistro extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}