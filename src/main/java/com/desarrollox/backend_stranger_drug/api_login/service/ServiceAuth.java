package com.desarrollox.backend_stranger_drug.api_login.service;

import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.desarrollox.backend_stranger_drug.api_login.exception.InvalidCredentialsException;
import com.desarrollox.backend_stranger_drug.api_login.exception.InvalidTokenException;
import com.desarrollox.backend_stranger_drug.api_login.models.AuthenticationResult;
import com.desarrollox.backend_stranger_drug.api_login.models.LoginAttempt;
import com.desarrollox.backend_stranger_drug.api_login.models.RefreshToken;
import com.desarrollox.backend_stranger_drug.api_login.repositories.IRepositoryLoginAttempt;
import com.desarrollox.backend_stranger_drug.api_login.repositories.IRepositoryRefreshToken;
import com.desarrollox.backend_stranger_drug.api_registro.exception.UserNotFoundException;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;
import com.desarrollox.backend_stranger_drug.api_registro.repositories.IRepositoryRegistro;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceAuth implements IServiceAuth {
    private final IRepositoryRegistro repositoryRegistro;
    private final PasswordEncoder passwordEncoder;
    private final IRepositoryLoginAttempt repositoryLoginAttempt;
    private final IRepositoryRefreshToken repositoryRefreshToken;
    private final IServiceToken serviceToken;
    
    @Override
    public AuthenticationResult authenticate(String email, String password, String ipAddress, String userAgent) {
        User user = repositoryRegistro.findByEmail(email).orElseThrow(() -> new UserNotFoundException("El usuario con email " + email + " no se encuentra registrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            recordFailedAttempt(email, ipAddress, userAgent, "Contraseña incorrecta");
            throw new InvalidCredentialsException(email, "Contraseña incorrecta");
        }

        //generar tokens
        String accessToken = serviceToken.generateAccessToken(user);
        String refreshToken = serviceToken.generateRefreshToken(user);

        //guardar refresh token en base de datos
        saveRefreshToken(refreshToken, user.getEmail());

        //registrar exito
        recordSuccessfulAttempt(email, ipAddress, userAgent);

        return AuthenticationResult.success(accessToken, refreshToken, serviceToken.getTokenExpirationTime(), user);
    }

    @Override
    public AuthenticationResult refreshToken(String refreshToken) {
        RefreshToken refresh = repositoryRefreshToken.findByToken(refreshToken).orElseThrow(() -> new InvalidTokenException("RefreshToken no encontrado"));

        if(!refresh.isValid()){
            throw new InvalidTokenException("RefreshToken inválido o expirado");
        }

        User user = repositoryRegistro.findByEmail(refresh.getUserEmail()).orElseThrow(() -> new InvalidTokenException("Usuario no encontrado para el RefreshToken"));

        //generar nuevos tokens
        String newAccessToken = serviceToken.generateAccessToken(user);
        String newRefreshToken = serviceToken.generateRefreshToken(user);

        //revocar el refresh token anterior
        refresh.revoke();
        repositoryRefreshToken.save(refresh);

        //guardar el nuevo refresh token
        saveRefreshToken(newRefreshToken, user.getEmail());

        return AuthenticationResult.success(newAccessToken, newRefreshToken, serviceToken.getTokenExpirationTime(), user);
    }

    @Override
    public Void logout(String refreshToken) {
        RefreshToken refresh = repositoryRefreshToken.findByToken(refreshToken).orElseThrow(() -> new InvalidTokenException("RefreshToken no encontrado"));
        refresh.revoke();
        repositoryRefreshToken.save(refresh);
        return null;
    }

    @Override
    public Boolean validateToken(String token) {
        boolean isValid = serviceToken.validateToken(token);
        return isValid;
    }

    private void saveRefreshToken(String refreshToken, String email) {
        RefreshToken refresh = RefreshToken.builder()
            .token(refreshToken)
            .userEmail(email)
            .expiryDate(LocalDateTime.now().plusDays(30))
            .revoked(false)
            .createdAt(LocalDateTime.now())
            .build();

        repositoryRefreshToken.save(refresh);
    }

    private void recordFailedAttempt(String email, String ipAddress, String userAgent, String message) {
        LoginAttempt attempt = LoginAttempt.failed(email, ipAddress, userAgent, message);
        repositoryLoginAttempt.save(attempt);
    }

    private void recordSuccessfulAttempt(String email, String ipAddress, String userAgent) {
        LoginAttempt attempt = LoginAttempt.successful(email, ipAddress, userAgent);
        repositoryLoginAttempt.save(attempt);
    }
}