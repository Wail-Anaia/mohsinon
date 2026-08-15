package com.mohsinon.core.security;

import com.mohsinon.modules.identity.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TokenProviderTest {

    private TokenProvider tokenProvider;
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret("test-secret-key-that-is-long-enough-for-sha256-signing-mohsinon-2026");
        jwtProperties.setAccessTokenExpirationSeconds(900);
        jwtProperties.setRefreshTokenExpirationSeconds(604800);
        tokenProvider = new TokenProvider(jwtProperties);
    }

    @Test
    @DisplayName("Should generate valid access token and extract user claims")
    void shouldGenerateAndValidateAccessToken() {
        User user = new User("ahmed_v", "ahmed@example.com", "hash", "Ahmed", "Benali", null);
        user.setId(UUID.randomUUID());

        String token = tokenProvider.generateAccessToken(user);

        assertThat(token).isNotBlank();
        assertThat(tokenProvider.validateAccessToken(token)).isTrue();
        assertThat(tokenProvider.getUserIdFromToken(token)).isEqualTo(user.getId());
        assertThat(tokenProvider.getUsernameFromToken(token)).isEqualTo("ahmed_v");
    }

    @Test
    @DisplayName("Should reject invalid or tampered access token")
    void shouldRejectInvalidToken() {
        assertThat(tokenProvider.validateAccessToken("invalid.jwt.token")).isFalse();
        assertThat(tokenProvider.validateAccessToken("")).isFalse();
    }

    @Test
    @DisplayName("Should generate distinct random opaque refresh tokens")
    void shouldGenerateUniqueRefreshTokens() {
        String token1 = tokenProvider.generateRawRefreshToken();
        String token2 = tokenProvider.generateRawRefreshToken();

        assertThat(token1).isNotBlank();
        assertThat(token2).isNotBlank();
        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    @DisplayName("Should compute consistent SHA-256 token hash")
    void shouldHashTokenConsistently() {
        String rawToken = "sample_secure_refresh_token_12345";
        String hash1 = tokenProvider.hashToken(rawToken);
        String hash2 = tokenProvider.hashToken(rawToken);

        assertThat(hash1).isEqualTo(hash2);
        assertThat(hash1).hasSize(64); // 256 bits = 64 hex characters
    }
}
