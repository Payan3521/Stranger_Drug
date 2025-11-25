package com.desarrollox.backend_stranger_drug.api_login.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_login.models.RefreshToken;

@Repository
public interface IRepositoryRefreshToken extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String refreshToken);
    
}
