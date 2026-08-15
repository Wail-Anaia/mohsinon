package com.mohsinon.modules.identity.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohsinon.modules.identity.repository.RefreshTokenRepository;
import com.mohsinon.modules.identity.repository.UserRepository;
import com.mohsinon.modules.identity.web.dto.AuthResponse;
import com.mohsinon.modules.identity.web.dto.LoginRequest;
import com.mohsinon.modules.identity.web.dto.LogoutRequest;
import com.mohsinon.modules.identity.web.dto.RefreshTokenRequest;
import com.mohsinon.modules.identity.web.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void cleanUp() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Complete Identity Lifecycle: Register -> Login -> Me -> Refresh -> Logout")
    void shouldExecuteFullIdentityLifecycle() throws Exception {
        // 1. REGISTER
        RegisterRequest registerRequest = new RegisterRequest(
                "karim_b",
                "karim@example.com",
                "Password123!",
                "Karim",
                "Bennani"
        );

        MvcResult registerResult = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.user.username", is("karim_b")))
                .andExpect(jsonPath("$.user.email", is("karim@example.com")))
                .andReturn();

        AuthResponse registerResponse = objectMapper.readValue(
                registerResult.getResponse().getContentAsString(),
                AuthResponse.class
        );
        String accessToken = registerResponse.getAccessToken();
        String refreshToken = registerResponse.getRefreshToken();

        // 2. GET ME with valid Access Token
        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("karim_b")))
                .andExpect(jsonPath("$.displayName", is("Karim Bennani")));

        // 3. GET ME without token -> Expect 401 Unauthorized ProblemDetail
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.errorCode", is("UNAUTHORIZED")));

        // 4. LOGIN with username
        LoginRequest loginRequest = new LoginRequest("karim_b", "Password123!");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andReturn();

        AuthResponse loginResponse = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(),
                AuthResponse.class
        );
        String newRefreshToken = loginResponse.getRefreshToken();

        // 5. REFRESH TOKEN
        RefreshTokenRequest refreshReq = new RefreshTokenRequest(newRefreshToken);
        MvcResult refreshResult = mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andReturn();

        AuthResponse refreshResponse = objectMapper.readValue(
                refreshResult.getResponse().getContentAsString(),
                AuthResponse.class
        );
        String rotatedRefreshToken = refreshResponse.getRefreshToken();
        assertThat(rotatedRefreshToken).isNotEqualTo(newRefreshToken);

        // 6. RE-USING OLD REFRESH TOKEN -> Expect Fraud Detection & 401
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenRequest(newRefreshToken))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode", is("TOKEN_REUSE_DETECTED")));

        // 7. LOGOUT
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LogoutRequest(rotatedRefreshToken))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Logged out successfully.")));
    }

    @Test
    @DisplayName("Should return 409 Conflict when attempting to register existing email or username")
    void shouldRejectDuplicateRegistration() throws Exception {
        RegisterRequest initialRequest = new RegisterRequest("samir_k", "samir@example.com", "SecretPass123!", "Samir", "Kacem");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initialRequest)))
                .andExpect(status().isCreated());

        // Duplicate email
        RegisterRequest duplicateEmail = new RegisterRequest("different_user", "samir@example.com", "SecretPass123!", "Samir", "Kacem");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateEmail)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode", is("USER_EMAIL_EXISTS")));

        // Duplicate username
        RegisterRequest duplicateUsername = new RegisterRequest("samir_k", "different@example.com", "SecretPass123!", "Samir", "Kacem");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateUsername)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode", is("USER_USERNAME_EXISTS")));
    }
}
