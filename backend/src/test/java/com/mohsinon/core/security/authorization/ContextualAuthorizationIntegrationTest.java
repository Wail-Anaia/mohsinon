package com.mohsinon.core.security.authorization;

import com.mohsinon.core.security.TokenProvider;
import com.mohsinon.core.security.authorization.entity.GlobalRole;
import com.mohsinon.core.security.authorization.entity.Membership;
import com.mohsinon.core.security.authorization.entity.UserGlobalRole;
import com.mohsinon.core.security.authorization.model.GlobalRoleType;
import com.mohsinon.core.security.authorization.model.MembershipRole;
import com.mohsinon.core.security.authorization.repository.GlobalRoleRepository;
import com.mohsinon.core.security.authorization.repository.MembershipRepository;
import com.mohsinon.core.security.authorization.repository.UserGlobalRoleRepository;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserStatus;
import com.mohsinon.modules.identity.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(ContextualAuthorizationIntegrationTest.TestMosqueSecurityController.class)
class ContextualAuthorizationIntegrationTest {

    @RestController
    @RequestMapping("/api/v1/test/authz/mosques")
    static class TestMosqueSecurityController {

        @GetMapping("/{mosqueId}")
        @PreAuthorize("@authz.hasPermission(principal, 'MOSQUE_VIEW', 'MOSQUE', #mosqueId)")
        public ResponseEntity<Map<String, String>> viewMosque(@PathVariable UUID mosqueId) {
            return ResponseEntity.ok(Map.of("status", "success", "action", "view", "mosqueId", mosqueId.toString()));
        }

        @PutMapping("/{mosqueId}")
        @PreAuthorize("@authz.canManageMosque(principal, #mosqueId)")
        public ResponseEntity<Map<String, String>> updateMosque(@PathVariable UUID mosqueId) {
            return ResponseEntity.ok(Map.of("status", "success", "action", "update", "mosqueId", mosqueId.toString()));
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GlobalRoleRepository globalRoleRepository;

    @Autowired
    private UserGlobalRoleRepository userGlobalRoleRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private TokenProvider tokenProvider;

    private User imamA;
    private User imamB;
    private User treasurerA;
    private User adminUser;
    private User suspendedUser;

    private String tokenImamA;
    private String tokenImamB;
    private String tokenTreasurerA;
    private String tokenAdmin;
    private String tokenSuspended;

    private UUID mosqueAId;
    private UUID mosqueBId;

    @BeforeEach
    void setUp() {
        membershipRepository.deleteAll();
        userGlobalRoleRepository.deleteAll();
        userRepository.deleteAll();
        globalRoleRepository.deleteAll();

        // Seed global roles
        GlobalRole roleUser = globalRoleRepository.save(new GlobalRole(GlobalRoleType.ROLE_USER, "Standard user"));
        GlobalRole roleAdmin = globalRoleRepository.save(new GlobalRole(GlobalRoleType.ROLE_ADMIN, "Admin"));

        mosqueAId = UUID.randomUUID();
        mosqueBId = UUID.randomUUID();

        // 1. Create Imam A
        imamA = userRepository.save(new User("imam_a", "imamA@example.com", "$2a$12$fakeHash", "Imam", "A", null));
        userGlobalRoleRepository.save(new UserGlobalRole(imamA.getId(), roleUser.getId(), null));
        membershipRepository.save(new Membership(imamA.getId(), "MOSQUE", mosqueAId, MembershipRole.IMAM, null));
        tokenImamA = tokenProvider.generateAccessToken(imamA);

        // 2. Create Imam B
        imamB = userRepository.save(new User("imam_b", "imamB@example.com", "$2a$12$fakeHash", "Imam", "B", null));
        userGlobalRoleRepository.save(new UserGlobalRole(imamB.getId(), roleUser.getId(), null));
        membershipRepository.save(new Membership(imamB.getId(), "MOSQUE", mosqueBId, MembershipRole.IMAM, null));
        tokenImamB = tokenProvider.generateAccessToken(imamB);

        // 3. Create Treasurer A (Mosque A)
        treasurerA = userRepository.save(new User("treasurer_a", "treasurerA@example.com", "$2a$12$fakeHash", "Treasurer", "A", null));
        userGlobalRoleRepository.save(new UserGlobalRole(treasurerA.getId(), roleUser.getId(), null));
        membershipRepository.save(new Membership(treasurerA.getId(), "MOSQUE", mosqueAId, MembershipRole.TREASURER, null));
        tokenTreasurerA = tokenProvider.generateAccessToken(treasurerA);

        // 4. Create Platform Super Admin
        adminUser = userRepository.save(new User("admin_user", "admin@example.com", "$2a$12$fakeHash", "Super", "Admin", null));
        userGlobalRoleRepository.save(new UserGlobalRole(adminUser.getId(), roleAdmin.getId(), null));
        tokenAdmin = tokenProvider.generateAccessToken(adminUser);

        // 5. Create Suspended User with Mosque A Membership
        suspendedUser = new User("suspended_u", "suspended@example.com", "$2a$12$fakeHash", "Suspended", "User", null);
        suspendedUser.setStatus(UserStatus.SUSPENDED);
        suspendedUser = userRepository.save(suspendedUser);
        userGlobalRoleRepository.save(new UserGlobalRole(suspendedUser.getId(), roleUser.getId(), null));
        membershipRepository.save(new Membership(suspendedUser.getId(), "MOSQUE", mosqueAId, MembershipRole.IMAM, null));
        tokenSuspended = tokenProvider.generateAccessToken(suspendedUser);
    }

    @Test
    @DisplayName("SECURITY REQUIREMENT: Imam A can manage Mosque A (ALLOW)")
    void imamACanManageMosqueA() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenImamA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action").value("update"))
                .andExpect(jsonPath("$.mosqueId").value(mosqueAId.toString()));
    }

    @Test
    @DisplayName("SECURITY REQUIREMENT: Imam A CANNOT manage Mosque B (DENY - 403 Forbidden)")
    void imamACannotManageMosqueB() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueBId)
                        .header("Authorization", "Bearer " + tokenImamA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("SECURITY REQUIREMENT: Imam B can manage Mosque B (ALLOW)")
    void imamBCanManageMosqueB() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueBId)
                        .header("Authorization", "Bearer " + tokenImamB)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action").value("update"))
                .andExpect(jsonPath("$.mosqueId").value(mosqueBId.toString()));
    }

    @Test
    @DisplayName("SECURITY REQUIREMENT: Imam B CANNOT manage Mosque A (DENY - 403 Forbidden)")
    void imamBCannotManageMosqueA() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenImamB)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Treasurer of Mosque A cannot manage Mosque A (lack of MOSQUE_UPDATE capability - 403 Forbidden)")
    void treasurerCannotUpdateMosque() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenTreasurerA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Unauthenticated request should return 401 Unauthorized")
    void unauthenticatedReturns401() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Suspended user token is rejected by security filter (DENY - 401 Unauthorized)")
    void suspendedUserDenied() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenSuspended)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Revoked membership should be denied (403 Forbidden)")
    void revokedMembershipDenied() throws Exception {
        // Revoke Imam A membership
        Membership membership = membershipRepository
                .findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(imamA.getId(), "MOSQUE", mosqueAId, MembershipRole.IMAM)
                .orElseThrow();
        membership.revoke();
        membershipRepository.save(membership);

        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenImamA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Expired membership should be denied (403 Forbidden)")
    void expiredMembershipDenied() throws Exception {
        // Expire Imam A membership
        Membership membership = membershipRepository
                .findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(imamA.getId(), "MOSQUE", mosqueAId, MembershipRole.IMAM)
                .orElseThrow();
        membership.setExpiresAt(Instant.now().minusSeconds(60));
        membershipRepository.save(membership);

        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenImamA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Platform Super Admin should be allowed to manage any mosque (Mosque A & Mosque B - ALLOW)")
    void adminCanManageAnyMosque() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueBId)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
