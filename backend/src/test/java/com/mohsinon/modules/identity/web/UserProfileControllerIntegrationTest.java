package com.mohsinon.modules.identity.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohsinon.core.domain.GeoLocation;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserProfile;
import com.mohsinon.modules.identity.domain.UserStatus;
import com.mohsinon.modules.identity.repository.RefreshTokenRepository;
import com.mohsinon.modules.identity.repository.UserProfileRepository;
import com.mohsinon.modules.identity.repository.UserRepository;
import com.mohsinon.modules.identity.web.dto.AuthResponse;
import com.mohsinon.modules.identity.web.dto.RegisterRequest;
import com.mohsinon.modules.identity.web.dto.UpdateUserProfileRequest;
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

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void cleanUp() {
        refreshTokenRepository.deleteAll();
        userProfileRepository.deleteAll();
        userRepository.deleteAll();
    }

    private String registerAndGetToken(String username, String email) throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                username,
                email,
                "Password123!",
                "First",
                "Last"
        );

        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        AuthResponse authResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AuthResponse.class
        );
        return authResponse.getAccessToken();
    }

    @Test
    @DisplayName("1. GET /api/v1/users/me (authenticated) should return 200 OK with private profile")
    void getMyProfileAuthenticated() throws Exception {
        String token = registerAndGetToken("tariq_m", "tariq@example.com");

        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("tariq_m")))
                .andExpect(jsonPath("$.email", is("tariq@example.com")))
                .andExpect(jsonPath("$.displayName", is("First Last")))
                .andExpect(jsonPath("$.preferredLanguage", is("fr")));
    }

    @Test
    @DisplayName("2. GET /api/v1/users/me (unauthenticated) should return 401 Unauthorized")
    void getMyProfileUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.errorCode", is("UNAUTHORIZED")));
    }

    @Test
    @DisplayName("3. PUT /api/v1/users/me (authenticated, valid) should update and return 200 OK")
    void updateMyProfileValid() throws Exception {
        String token = registerAndGetToken("samir_k", "samir@example.com");

        UpdateUserProfileRequest request = new UpdateUserProfileRequest(
                "Samir",
                "Kacem",
                "Samir K.",
                "Développeur engagé dans le bénévolat.",
                "https://cdn.mohsinon.org/samir.png",
                "+33612345678",
                "ar",
                "Lyon",
                "FR",
                45.764043,
                4.835659
        );

        mockMvc.perform(put("/api/v1/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("samir_k")))
                .andExpect(jsonPath("$.displayName", is("Samir K.")))
                .andExpect(jsonPath("$.bio", is("Développeur engagé dans le bénévolat.")))
                .andExpect(jsonPath("$.avatarUrl", is("https://cdn.mohsinon.org/samir.png")))
                .andExpect(jsonPath("$.phoneNumber", is("+33612345678")))
                .andExpect(jsonPath("$.preferredLanguage", is("ar")))
                .andExpect(jsonPath("$.city", is("Lyon")))
                .andExpect(jsonPath("$.countryCode", is("FR")))
                .andExpect(jsonPath("$.location.latitude", is(45.764043)))
                .andExpect(jsonPath("$.location.longitude", is(4.835659)));
    }

    @Test
    @DisplayName("4. PUT /api/v1/users/me (invalid request: bio > 500) should return 400 Bad Request")
    void updateMyProfileValidationFailure() throws Exception {
        String token = registerAndGetToken("karim_b", "karim@example.com");

        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setBio("A".repeat(501)); // Exceeds 500 characters limit

        mockMvc.perform(put("/api/v1/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errorCode", is("VALIDATION_FAILED")));
    }

    @Test
    @DisplayName("5. GET /api/v1/public/users/{username} (active user, public) should return 200 OK")
    void getPublicProfileActiveUser() throws Exception {
        // Register user and set a profile
        String token = registerAndGetToken("youssef_m", "youssef@example.com");

        UpdateUserProfileRequest updateRequest = new UpdateUserProfileRequest(
                "Youssef", "Mansour", "Youssef M.",
                "Coordinateur d'entraide.", "https://cdn.mohsinon.org/youssef.png",
                "+33699887766", "fr", "Paris", "FR",
                48.856614, 2.3522219
        );

        mockMvc.perform(put("/api/v1/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Now query public endpoint WITHOUT any Authorization token
        mockMvc.perform(get("/api/v1/public/users/youssef_m"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("youssef_m")))
                .andExpect(jsonPath("$.displayName", is("Youssef M.")))
                .andExpect(jsonPath("$.bio", is("Coordinateur d'entraide.")))
                .andExpect(jsonPath("$.avatarUrl", is("https://cdn.mohsinon.org/youssef.png")))
                .andExpect(jsonPath("$.city", is("Paris")))
                .andExpect(jsonPath("$.countryCode", is("FR")))
                .andExpect(jsonPath("$.memberSince", notNullValue()));
    }

    @Test
    @DisplayName("6. PRIVACY GUARANTEE: Public profile must NEVER contain email, phone or exact GPS")
    void publicProfilePrivacyGuarantee() throws Exception {
        String token = registerAndGetToken("hassan_b", "hassan@example.com");

        UpdateUserProfileRequest updateRequest = new UpdateUserProfileRequest(
                "Hassan", "B", "Hassan",
                "Membre.", "https://cdn.mohsinon.org/h.png",
                "+33711223344", "fr", "Marseille", "FR",
                43.296482, 5.36978
        );

        mockMvc.perform(put("/api/v1/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Query public endpoint
        mockMvc.perform(get("/api/v1/public/users/hassan_b"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.phoneNumber").doesNotExist())
                .andExpect(jsonPath("$.status").doesNotExist())
                .andExpect(jsonPath("$.roles").doesNotExist())
                // Verify approximate location (rounded to 43.3 and 5.37), exact was 43.296482, 5.36978
                .andExpect(jsonPath("$.approximateLocation.latitude", is(43.3)))
                .andExpect(jsonPath("$.approximateLocation.longitude", is(5.37)));
    }

    @Test
    @DisplayName("7. GET /api/v1/public/users/{username} for unknown username should return 404 Not Found")
    void getPublicProfileUnknownUser() throws Exception {
        mockMvc.perform(get("/api/v1/public/users/non_existent_user"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.errorCode", is("RESOURCE_NOT_FOUND")));
    }

    @Test
    @DisplayName("8. Identity Isolation: /me always reflects authenticated user, immune to client manipulation")
    void identityIsolationForMeEndpoint() throws Exception {
        String tokenA = registerAndGetToken("user_a", "usera@example.com");
        String tokenB = registerAndGetToken("user_b", "userb@example.com");

        // User A calls /me
        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("user_a")))
                .andExpect(jsonPath("$.email", is("usera@example.com")));

        // User B calls /me
        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("user_b")))
                .andExpect(jsonPath("$.email", is("userb@example.com")));
    }

    @Test
    @DisplayName("9. Public profile for inactive/suspended user must return 404 Not Found")
    void publicProfileInactiveUserReturnsNotFound() throws Exception {
        registerAndGetToken("suspended_user", "suspended@example.com");
        User user = userRepository.findByUsername("suspended_user").orElseThrow();
        user.setStatus(UserStatus.SUSPENDED);
        userRepository.save(user);

        mockMvc.perform(get("/api/v1/public/users/suspended_user"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is("RESOURCE_NOT_FOUND")));
    }
}
