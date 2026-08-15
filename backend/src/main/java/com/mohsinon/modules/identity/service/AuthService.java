package com.mohsinon.modules.identity.service;

import com.mohsinon.core.exception.ConflictException;
import com.mohsinon.core.exception.ForbiddenException;
import com.mohsinon.core.exception.ResourceNotFoundException;
import com.mohsinon.core.exception.UnauthorizedException;
import com.mohsinon.core.security.TokenProvider;
import com.mohsinon.modules.identity.domain.RefreshToken;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserStatus;
import com.mohsinon.modules.identity.repository.RefreshTokenRepository;
import com.mohsinon.modules.identity.repository.UserRepository;
import com.mohsinon.modules.identity.web.dto.AuthResponse;
import com.mohsinon.modules.identity.web.dto.LoginRequest;
import com.mohsinon.modules.identity.web.dto.RefreshTokenRequest;
import com.mohsinon.modules.identity.web.dto.RegisterRequest;
import com.mohsinon.modules.identity.web.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Registers a new user account with unique email and username.
     */
    public AuthResponse register(RegisterRequest request, String ipAddress, String userAgent) {
        String normalizedEmail = request.getEmail().trim().toLowerCase(Locale.ROOT);
        String normalizedUsername = request.getUsername().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("USER_EMAIL_EXISTS", "An account with this email address already exists.");
        }

        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new ConflictException("USER_USERNAME_EXISTS", "This username is already taken.");
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());
        User user = new User(
                normalizedUsername,
                normalizedEmail,
                passwordHash,
                request.getFirstName(),
                request.getLastName(),
                null
        );
        user = userRepository.save(user);
        log.info("Registered new user with id: {} and username: {}", user.getId(), user.getUsername());

        return createAuthSession(user, ipAddress, userAgent);
    }

    /**
     * Authenticates an existing user and issues access and refresh tokens.
     */
    public AuthResponse login(LoginRequest request, String ipAddress, String userAgent) {
        String identifier = request.getIdentifier().trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UnauthorizedException("INVALID_CREDENTIALS", "Invalid email/username or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Failed login attempt for identifier: {}", identifier);
            throw new UnauthorizedException("INVALID_CREDENTIALS", "Invalid email/username or password.");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            log.warn("Blocked login for inactive/suspended user: {} (Status: {})", user.getId(), user.getStatus());
            throw new ForbiddenException("ACCOUNT_INACTIVE",
                    String.format("Your account is currently %s. Please contact support.", user.getStatus().name().toLowerCase()));
        }

        log.info("Successful login for user: {}", user.getId());
        return createAuthSession(user, ipAddress, userAgent);
    }

    /**
     * Performs atomic refresh token rotation with reuse detection.
     */
    public AuthResponse refreshToken(RefreshTokenRequest request, String ipAddress, String userAgent) {
        String rawToken = request.getRefreshToken();
        String tokenHash = tokenProvider.hashToken(rawToken);

        RefreshToken existingToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new UnauthorizedException("INVALID_REFRESH_TOKEN", "The provided refresh token is invalid."));

        // Reuse / Fraud detection
        if (existingToken.isRevoked() || existingToken.getReplacedByTokenHash() != null) {
            log.warn("SECURITY ALERT: Detected refresh token reuse for user: {}! Revoking all sessions.", existingToken.getUserId());
            refreshTokenRepository.revokeAllActiveByUserId(existingToken.getUserId(), Instant.now());
            throw new UnauthorizedException("TOKEN_REUSE_DETECTED", "Security violation: This refresh token was already used or revoked.");
        }

        if (existingToken.isExpired()) {
            throw new UnauthorizedException("EXPIRED_REFRESH_TOKEN", "The refresh token has expired. Please log in again.");
        }

        User user = userRepository.findById(existingToken.getUserId())
                .orElseThrow(() -> new UnauthorizedException("USER_NOT_FOUND", "Associated user account was not found."));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ForbiddenException("ACCOUNT_INACTIVE", "Your account is not active.");
        }

        // Generate new refresh token
        String newRawRefreshToken = tokenProvider.generateRawRefreshToken();
        String newTokenHash = tokenProvider.hashToken(newRawRefreshToken);
        Instant newExpiresAt = Instant.now().plusSeconds(tokenProvider.getRefreshTokenExpirationSeconds());

        // Replace and revoke old token
        existingToken.replaceWith(newTokenHash);
        refreshTokenRepository.save(existingToken);

        // Save new token
        RefreshToken newRefreshToken = new RefreshToken(user.getId(), newTokenHash, newExpiresAt, ipAddress, userAgent);
        refreshTokenRepository.save(newRefreshToken);

        String newAccessToken = tokenProvider.generateAccessToken(user);

        return new AuthResponse(
                newAccessToken,
                newRawRefreshToken,
                tokenProvider.getAccessTokenExpirationSeconds(),
                UserResponse.fromUser(user)
        );
    }

    /**
     * Revokes a specific refresh token or user session.
     */
    public void logout(String rawRefreshToken, UUID currentUserId) {
        if (rawRefreshToken != null && !rawRefreshToken.trim().isEmpty()) {
            String tokenHash = tokenProvider.hashToken(rawRefreshToken);
            refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(token -> {
                token.revoke();
                refreshTokenRepository.save(token);
                log.info("Revoked refresh token for user: {}", token.getUserId());
            });
        } else if (currentUserId != null) {
            refreshTokenRepository.revokeAllActiveByUserId(currentUserId, Instant.now());
            log.info("Revoked all active refresh tokens for user: {}", currentUserId);
        }
    }

    /**
     * Retrieves the profile of the current authenticated user.
     */
    @Transactional(readOnly = true)
    public UserResponse getProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return UserResponse.fromUser(user);
    }

    private AuthResponse createAuthSession(User user, String ipAddress, String userAgent) {
        String accessToken = tokenProvider.generateAccessToken(user);
        String rawRefreshToken = tokenProvider.generateRawRefreshToken();
        String tokenHash = tokenProvider.hashToken(rawRefreshToken);

        Instant expiresAt = Instant.now().plusSeconds(tokenProvider.getRefreshTokenExpirationSeconds());
        RefreshToken refreshToken = new RefreshToken(user.getId(), tokenHash, expiresAt, ipAddress, userAgent);
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                accessToken,
                rawRefreshToken,
                tokenProvider.getAccessTokenExpirationSeconds(),
                UserResponse.fromUser(user)
        );
    }
}
