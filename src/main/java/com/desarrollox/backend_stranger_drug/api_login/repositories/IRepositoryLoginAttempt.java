package com.desarrollox.backend_stranger_drug.api_login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_login.models.LoginAttempt;

@Repository
public interface IRepositoryLoginAttempt extends JpaRepository<LoginAttempt, Long> {
    
}
