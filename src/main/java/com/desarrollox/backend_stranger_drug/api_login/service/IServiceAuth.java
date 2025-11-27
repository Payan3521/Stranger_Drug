package com.desarrollox.backend_stranger_drug.api_login.service;

import com.desarrollox.backend_stranger_drug.api_login.models.AuthenticationResult;

public interface IServiceAuth {
    AuthenticationResult authenticate(String email, String password, String ipAddress, String userAgent);
    AuthenticationResult refreshToken(String refreshToken);
    Void logout(String refreshToken);
    Boolean validateToken(String token);
}