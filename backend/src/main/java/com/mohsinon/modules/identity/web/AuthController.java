package com.mohsinon.modules.identity.web;

import com.mohsinon.core.security.CurrentUserProvider;
import com.mohsinon.modules.identity.service.AuthService;
import com.mohsinon.modules.identity.web.dto.AuthResponse;
import com.mohsinon.modules.identity.web.dto.LoginRequest;
import com.mohsinon.modules.identity.web.dto.LogoutRequest;
import com.mohsinon.modules.identity.web.dto.RefreshTokenRequest;
import com.mohsinon.modules.identity.web.dto.RegisterRequest;
import com.mohsinon.modules.identity.web.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication & Identity", description = "Endpoints for user registration, authentication, token rotation, and profile recovery")
public class AuthController {

    private final AuthService authService;
    private final CurrentUserProvider currentUserProvider;

    public AuthController(AuthService authService, CurrentUserProvider currentUserProvider) {
        this.authService = authService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user account", description = "Creates a new user and issues initial access and refresh tokens.")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request,
                                                 HttpServletRequest httpRequest) {
        String ipAddress = extractClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        AuthResponse response = authService.register(request, ipAddress, userAgent);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user credentials", description = "Validates username/email and password, returning JWT access token and refresh token.")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletRequest httpRequest) {
        String ipAddress = extractClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        AuthResponse response = authService.login(request, ipAddress, userAgent);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh expired access token", description = "Exchanges a valid refresh token for a new access token and a rotated refresh token.")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request,
                                                     HttpServletRequest httpRequest) {
        String ipAddress = extractClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        AuthResponse response = authService.refreshToken(request, ipAddress, userAgent);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Revoke user session", description = "Revokes the specified refresh token or all active sessions for the current authenticated user.")
    public ResponseEntity<Map<String, String>> logout(@RequestBody(required = false) LogoutRequest request) {
        UUID currentUserId = currentUserProvider.getCurrentUserId().orElse(null);
        String refreshToken = request != null ? request.getRefreshToken() : null;

        authService.logout(refreshToken, currentUserId);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully."));
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Get current authenticated user profile", description = "Returns identity details of the user identified by the Bearer token.")
    public ResponseEntity<UserResponse> getCurrentUser() {
        UUID currentUserId = currentUserProvider.requireCurrentUserId();
        UserResponse response = authService.getProfile(currentUserId);
        return ResponseEntity.ok(response);
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.trim().isEmpty()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
