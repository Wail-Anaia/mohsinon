package com.mohsinon.modules.identity.service;

import com.mohsinon.core.exception.ConflictException;
import com.mohsinon.core.exception.ForbiddenException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User("youssef_m", "youssef@example.com", "$2a$12$hashedPassword", "Youssef", "Mansour", null);
        sampleUser.setId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void shouldRegisterNewUser() {
        RegisterRequest request = new RegisterRequest("youssef_m", "youssef@example.com", "Password123!", "Youssef", "Mansour");

        when(userRepository.existsByEmail("youssef@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("youssef_m")).thenReturn(false);
        when(passwordEncoder.encode("Password123!")).thenReturn("$2a$12$hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(tokenProvider.generateAccessToken(any(User.class))).thenReturn("mock-access-token");
        when(tokenProvider.generateRawRefreshToken()).thenReturn("mock-raw-refresh-token");
        when(tokenProvider.hashToken("mock-raw-refresh-token")).thenReturn("mock-hash");
        when(tokenProvider.getAccessTokenExpirationSeconds()).thenReturn(900L);
        when(tokenProvider.getRefreshTokenExpirationSeconds()).thenReturn(604800L);

        AuthResponse response = authService.register(request, "127.0.0.1", "JUnit");

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("mock-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("mock-raw-refresh-token");
        assertThat(response.getUser().getEmail()).isEqualTo("youssef@example.com");
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Should throw ConflictException when email is already registered")
    void shouldRejectDuplicateEmail() {
        RegisterRequest request = new RegisterRequest("new_user", "youssef@example.com", "Password123!", "Youssef", "Mansour");
        when(userRepository.existsByEmail("youssef@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request, "127.0.0.1", "JUnit"))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("email address already exists");
    }

    @Test
    @DisplayName("Should throw ConflictException when username is already taken")
    void shouldRejectDuplicateUsername() {
        RegisterRequest request = new RegisterRequest("youssef_m", "new@example.com", "Password123!", "Youssef", "Mansour");
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("youssef_m")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request, "127.0.0.1", "JUnit"))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("username is already taken");
    }

    @Test
    @DisplayName("Should successfully login with valid identifier and password")
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("youssef@example.com", "Password123!");

        when(userRepository.findByIdentifier("youssef@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("Password123!", sampleUser.getPasswordHash())).thenReturn(true);
        when(tokenProvider.generateAccessToken(sampleUser)).thenReturn("mock-access-token");
        when(tokenProvider.generateRawRefreshToken()).thenReturn("mock-raw-refresh-token");
        when(tokenProvider.hashToken("mock-raw-refresh-token")).thenReturn("mock-hash");
        when(tokenProvider.getAccessTokenExpirationSeconds()).thenReturn(900L);
        when(tokenProvider.getRefreshTokenExpirationSeconds()).thenReturn(604800L);

        AuthResponse response = authService.login(request, "127.0.0.1", "JUnit");

        assertThat(response.getAccessToken()).isEqualTo("mock-access-token");
        assertThat(response.getUser().getUsername()).isEqualTo("youssef_m");
    }

    @Test
    @DisplayName("Should reject login with invalid password")
    void shouldRejectInvalidPassword() {
        LoginRequest request = new LoginRequest("youssef@example.com", "WrongPassword");

        when(userRepository.findByIdentifier("youssef@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("WrongPassword", sampleUser.getPasswordHash())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request, "127.0.0.1", "JUnit"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Invalid email/username or password");
    }

    @Test
    @DisplayName("Should reject login when account is suspended")
    void shouldRejectSuspendedUserLogin() {
        sampleUser.setStatus(UserStatus.SUSPENDED);
        LoginRequest request = new LoginRequest("youssef@example.com", "Password123!");

        when(userRepository.findByIdentifier("youssef@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("Password123!", sampleUser.getPasswordHash())).thenReturn(true);

        assertThatThrownBy(() -> authService.login(request, "127.0.0.1", "JUnit"))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("suspended");
    }

    @Test
    @DisplayName("Should rotate refresh token correctly on refresh request")
    void shouldRotateRefreshToken() {
        RefreshTokenRequest request = new RefreshTokenRequest("raw-refresh-token");
        RefreshToken existingToken = new RefreshToken(sampleUser.getId(), "old-hash", Instant.now().plusSeconds(3600), "127.0.0.1", "JUnit");

        when(tokenProvider.hashToken("raw-refresh-token")).thenReturn("old-hash");
        when(refreshTokenRepository.findByTokenHash("old-hash")).thenReturn(Optional.of(existingToken));
        when(userRepository.findById(sampleUser.getId())).thenReturn(Optional.of(sampleUser));
        when(tokenProvider.generateRawRefreshToken()).thenReturn("new-raw-refresh-token");
        when(tokenProvider.hashToken("new-raw-refresh-token")).thenReturn("new-hash");
        when(tokenProvider.generateAccessToken(sampleUser)).thenReturn("new-access-token");
        when(tokenProvider.getAccessTokenExpirationSeconds()).thenReturn(900L);
        when(tokenProvider.getRefreshTokenExpirationSeconds()).thenReturn(604800L);

        AuthResponse response = authService.refreshToken(request, "127.0.0.1", "JUnit");

        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("new-raw-refresh-token");
        assertThat(existingToken.isRevoked()).isTrue();
        assertThat(existingToken.getReplacedByTokenHash()).isEqualTo("new-hash");
    }

    @Test
    @DisplayName("Should detect refresh token reuse, revoke all user sessions, and reject request")
    void shouldDetectTokenReuseAndRevokeAllSessions() {
        RefreshTokenRequest request = new RefreshTokenRequest("stolen-reused-token");
        RefreshToken alreadyReplacedToken = new RefreshToken(sampleUser.getId(), "stolen-hash", Instant.now().plusSeconds(3600), "127.0.0.1", "JUnit");
        alreadyReplacedToken.replaceWith("another-newer-hash");

        when(tokenProvider.hashToken("stolen-reused-token")).thenReturn("stolen-hash");
        when(refreshTokenRepository.findByTokenHash("stolen-hash")).thenReturn(Optional.of(alreadyReplacedToken));

        assertThatThrownBy(() -> authService.refreshToken(request, "127.0.0.1", "JUnit"))
                .isInstanceOf(UnauthorizedException.class)
                .matches(ex -> "TOKEN_REUSE_DETECTED".equals(((UnauthorizedException) ex).getErrorCode()))
                .hasMessageContaining("already used or revoked");

        verify(refreshTokenRepository).revokeAllActiveByUserId(eq(sampleUser.getId()), any(Instant.class));
    }
}
