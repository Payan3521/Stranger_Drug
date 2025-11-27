package com.desarrollox.backend_stranger_drug.api_login.service;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;

public interface IServiceToken {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    Long getTokenExpirationTime();
    boolean validateToken(String token);
}
