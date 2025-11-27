package com.desarrollox.backend_stranger_drug.api_login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend_stranger_drug.api_login.controller.dto.ApiResponse;
import com.desarrollox.backend_stranger_drug.api_login.controller.dto.LoginRequest;
import com.desarrollox.backend_stranger_drug.api_login.controller.dto.LoginResponse;
import com.desarrollox.backend_stranger_drug.api_login.controller.dto.LogoutRequest;
import com.desarrollox.backend_stranger_drug.api_login.controller.dto.RefreshTokenRequest;
import com.desarrollox.backend_stranger_drug.api_login.controller.webMapper.AuthenticationWebMapper;
import com.desarrollox.backend_stranger_drug.api_login.models.AuthenticationResult;
import com.desarrollox.backend_stranger_drug.api_login.service.IServiceAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ControllerAuth {

    private final IServiceAuth serviceAuth;
    private final AuthenticationWebMapper authenticationWebMapper;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        AuthenticationResult result = serviceAuth.authenticate(loginRequest.getEmail(), loginRequest.getPassword(), ipAddress, userAgent);
        LoginResponse response = authenticationWebMapper.toLoginResponse(result);

        return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthenticationResult result = serviceAuth.refreshToken(refreshTokenRequest.getRefreshToken());
        LoginResponse response = authenticationWebMapper.toLoginResponse(result);

        return ResponseEntity.ok(ApiResponse.success("Token renovado exitosamente", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        serviceAuth.logout(logoutRequest.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Logout exitoso"));
    }

    @PostMapping("/check-token")
    public ResponseEntity<ApiResponse<Boolean>> checkToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        boolean isValid = serviceAuth.validateToken(token);
        return ResponseEntity.ok(ApiResponse.success("Token validado exitosamente", isValid));
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
