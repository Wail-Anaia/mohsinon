package com.mohsinon.core.security.authorization;

import com.mohsinon.core.security.TokenProvider;
import com.mohsinon.core.security.authorization.entity.GlobalRole;
import com.mohsinon.core.security.authorization.entity.Membership;
import com.mohsinon.core.security.authorization.entity.UserGlobalRole;
import com.mohsinon.core.security.authorization.model.GlobalRoleType;
import com.mohsinon.core.security.authorization.model.MembershipRole;
import com.mohsinon.core.security.authorization.model.MembershipStatus;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(ContextualAuthorizationIntegrationTest.TestSecurityController.class)
class ContextualAuthorizationIntegrationTest {

    @RestController
    @RequestMapping("/api/v1/test/authz")
    static class TestSecurityController {

        @GetMapping("/mosques/{mosqueId}")
        @PreAuthorize("@authz.hasPermission(principal, 'MOSQUE_VIEW', 'MOSQUE', #mosqueId)")
        public ResponseEntity<Map<String, String>> viewMosque(@PathVariable UUID mosqueId) {
            return ResponseEntity.ok(Map.of("status", "success", "action", "view", "mosqueId", mosqueId.toString()));
        }

        @PutMapping("/mosques/{mosqueId}")
        @PreAuthorize("@authz.canManageMosque(principal, #mosqueId)")
        public ResponseEntity<Map<String, String>> updateMosque(@PathVariable UUID mosqueId) {
            return ResponseEntity.ok(Map.of("status", "success", "action", "update", "mosqueId", mosqueId.toString()));
        }

        @PutMapping("/projects/{projectId}")
        @PreAuthorize("@authz.hasPermission(principal, 'PROJECT_MANAGE', 'PROJECT', #projectId)")
        public ResponseEntity<Map<String, String>> manageProject(@PathVariable UUID projectId) {
            return ResponseEntity.ok(Map.of("status", "success", "action", "manage_project", "projectId", projectId.toString()));
        }

        @PutMapping("/mosques/{mosqueId}/donations")
        @PreAuthorize("@authz.hasPermission(principal, 'DONATION_MANAGE', 'MOSQUE', #mosqueId)")
        public ResponseEntity<Map<String, String>> manageDonations(@PathVariable UUID mosqueId) {
            return ResponseEntity.ok(Map.of("status", "success", "action", "manage_donations", "mosqueId", mosqueId.toString()));
        }

        @PutMapping("/mosques/{mosqueId}/volunteers")
        @PreAuthorize("@authz.hasPermission(principal, 'VOLUNTEER_MANAGE', 'MOSQUE', #mosqueId)")
        public ResponseEntity<Map<String, String>> manageVolunteers(@PathVariable UUID mosqueId) {
            return ResponseEntity.ok(Map.of("status", "success", "action", "manage_volunteers", "mosqueId", mosqueId.toString()));
        }

        @PostMapping("/global/initiatives")
        @PreAuthorize("@authz.hasGlobalPermission(principal, 'INITIATIVE_CREATE')")
        public ResponseEntity<Map<String, String>> createGlobalInitiative() {
            return ResponseEntity.ok(Map.of("status", "success", "action", "create_initiative"));
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
    private User volunteerCoordA;
    private User standardUser;
    private User adminUser;
    private User suspendedUser;
    private User inactiveUser;

    private String tokenImamA;
    private String tokenImamB;
    private String tokenTreasurerA;
    private String tokenVolunteerCoordA;
    private String tokenStandardUser;
    private String tokenAdmin;
    private String tokenSuspended;
    private String tokenInactive;

    private UUID mosqueAId;
    private UUID mosqueBId;
    private UUID projectAId;

    @BeforeEach
    void setUp() {
        membershipRepository.deleteAll();
        userGlobalRoleRepository.deleteAll();
        userRepository.deleteAll();
        globalRoleRepository.deleteAll();

        // Seed global roles
        GlobalRole roleUser = globalRoleRepository.save(new GlobalRole(GlobalRoleType.ROLE_USER, "Standard user"));
        GlobalRole roleAdmin = globalRoleRepository.save(new GlobalRole(GlobalRoleType.ROLE_ADMIN, "Admin"));
        GlobalRole roleVolunteer = globalRoleRepository.save(new GlobalRole(GlobalRoleType.ROLE_VOLUNTEER, "Volunteer"));

        mosqueAId = UUID.randomUUID();
        mosqueBId = UUID.randomUUID();
        projectAId = UUID.randomUUID();

        // 1. Create Imam A (Mosque A)
        imamA = userRepository.save(new User("imam_a", "imamA@example.com", "$2a$12$fakeHash", "Imam", "A", null));
        userGlobalRoleRepository.save(new UserGlobalRole(imamA.getId(), roleUser.getId(), null));
        membershipRepository.save(new Membership(imamA.getId(), "MOSQUE", mosqueAId, MembershipRole.IMAM, null));
        tokenImamA = tokenProvider.generateAccessToken(imamA);

        // 2. Create Imam B (Mosque B)
        imamB = userRepository.save(new User("imam_b", "imamB@example.com", "$2a$12$fakeHash", "Imam", "B", null));
        userGlobalRoleRepository.save(new UserGlobalRole(imamB.getId(), roleUser.getId(), null));
        membershipRepository.save(new Membership(imamB.getId(), "MOSQUE", mosqueBId, MembershipRole.IMAM, null));
        tokenImamB = tokenProvider.generateAccessToken(imamB);

        // 3. Create Treasurer A (Mosque A)
        treasurerA = userRepository.save(new User("treasurer_a", "treasurerA@example.com", "$2a$12$fakeHash", "Treasurer", "A", null));
        userGlobalRoleRepository.save(new UserGlobalRole(treasurerA.getId(), roleUser.getId(), null));
        membershipRepository.save(new Membership(treasurerA.getId(), "MOSQUE", mosqueAId, MembershipRole.TREASURER, null));
        tokenTreasurerA = tokenProvider.generateAccessToken(treasurerA);

        // 4. Create Volunteer Coordinator A (Mosque A)
        volunteerCoordA = userRepository.save(new User("vol_coord_a", "volcoordA@example.com", "$2a$12$fakeHash", "Coord", "A", null));
        userGlobalRoleRepository.save(new UserGlobalRole(volunteerCoordA.getId(), roleVolunteer.getId(), null));
        membershipRepository.save(new Membership(volunteerCoordA.getId(), "MOSQUE", mosqueAId, MembershipRole.VOLUNTEER_COORDINATOR, null));
        tokenVolunteerCoordA = tokenProvider.generateAccessToken(volunteerCoordA);

        // 5. Create Standard Community User
        standardUser = userRepository.save(new User("std_user", "stduser@example.com", "$2a$12$fakeHash", "Standard", "User", null));
        userGlobalRoleRepository.save(new UserGlobalRole(standardUser.getId(), roleUser.getId(), null));
        tokenStandardUser = tokenProvider.generateAccessToken(standardUser);

        // 6. Create Platform Super Admin
        adminUser = userRepository.save(new User("admin_user", "admin@example.com", "$2a$12$fakeHash", "Super", "Admin", null));
        userGlobalRoleRepository.save(new UserGlobalRole(adminUser.getId(), roleAdmin.getId(), null));
        tokenAdmin = tokenProvider.generateAccessToken(adminUser);

        // 7. Create Suspended User with Mosque A Membership
        suspendedUser = new User("suspended_u", "suspended@example.com", "$2a$12$fakeHash", "Suspended", "User", null);
        suspendedUser.setStatus(UserStatus.SUSPENDED);
        suspendedUser = userRepository.save(suspendedUser);
        userGlobalRoleRepository.save(new UserGlobalRole(suspendedUser.getId(), roleUser.getId(), null));
        membershipRepository.save(new Membership(suspendedUser.getId(), "MOSQUE", mosqueAId, MembershipRole.IMAM, null));
        tokenSuspended = tokenProvider.generateAccessToken(suspendedUser);

        // 8. Create Inactive User
        inactiveUser = new User("inactive_u", "inactive@example.com", "$2a$12$fakeHash", "Inactive", "User", null);
        inactiveUser.setStatus(UserStatus.INACTIVE);
        inactiveUser = userRepository.save(inactiveUser);
        userGlobalRoleRepository.save(new UserGlobalRole(inactiveUser.getId(), roleUser.getId(), null));
        membershipRepository.save(new Membership(inactiveUser.getId(), "MOSQUE", mosqueAId, MembershipRole.IMAM, null));
        tokenInactive = tokenProvider.generateAccessToken(inactiveUser);
    }

    // --- Scenario 1: Cross-Mosque Isolation ---

    @Test
    @DisplayName("SECURITY 1.1: Imam A can manage Mosque A (ALLOW)")
    void imamACanManageMosqueA() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenImamA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action").value("update"))
                .andExpect(jsonPath("$.mosqueId").value(mosqueAId.toString()));
    }

    @Test
    @DisplayName("SECURITY 1.2: Imam A CANNOT manage Mosque B (DENY - 403 Forbidden)")
    void imamACannotManageMosqueB() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueBId)
                        .header("Authorization", "Bearer " + tokenImamA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("SECURITY 1.3: Imam B can manage Mosque B (ALLOW)")
    void imamBCanManageMosqueB() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueBId)
                        .header("Authorization", "Bearer " + tokenImamB)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action").value("update"))
                .andExpect(jsonPath("$.mosqueId").value(mosqueBId.toString()));
    }

    @Test
    @DisplayName("SECURITY 1.4: Imam B CANNOT manage Mosque A (DENY - 403 Forbidden)")
    void imamBCannotManageMosqueA() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenImamB)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // --- Scenario 2: Resource-Type Isolation ---

    @Test
    @DisplayName("SECURITY 2.1: Mosque A membership must NOT authorize managing Project A (DENY - 403 Forbidden)")
    void mosqueMembershipCannotManageProject() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/projects/" + projectAId)
                        .header("Authorization", "Bearer " + tokenImamA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // --- Scenario 3: Membership Lifecycle Security ---

    @Test
    @DisplayName("SECURITY 3.1: PENDING_APPROVAL membership must be DENIED (403 Forbidden)")
    void pendingApprovalMembershipDenied() throws Exception {
        Membership membership = membershipRepository
                .findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(imamA.getId(), "MOSQUE", mosqueAId, MembershipRole.IMAM)
                .orElseThrow();
        membership.setStatus(MembershipStatus.PENDING_APPROVAL);
        membershipRepository.save(membership);

        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenImamA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("SECURITY 3.2: REVOKED membership must be DENIED (403 Forbidden)")
    void revokedMembershipDenied() throws Exception {
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
    @DisplayName("SECURITY 3.3: EXPIRED membership status must be DENIED (403 Forbidden)")
    void expiredStatusMembershipDenied() throws Exception {
        Membership membership = membershipRepository
                .findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(imamA.getId(), "MOSQUE", mosqueAId, MembershipRole.IMAM)
                .orElseThrow();
        membership.expire();
        membershipRepository.save(membership);

        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenImamA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("SECURITY 3.4: ACTIVE membership with past expiresAt date must be DENIED (403 Forbidden)")
    void expiredDateMembershipDenied() throws Exception {
        Membership membership = membershipRepository
                .findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(imamA.getId(), "MOSQUE", mosqueAId, MembershipRole.IMAM)
                .orElseThrow();
        membership.setExpiresAt(Instant.now().minusSeconds(120));
        membershipRepository.save(membership);

        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenImamA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // --- Scenario 4: Permission Boundary ---

    @Test
    @DisplayName("SECURITY 4.1: Treasurer can manage donations on Mosque A (ALLOW)")
    void treasurerCanManageDonations() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId + "/donations")
                        .header("Authorization", "Bearer " + tokenTreasurerA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action").value("manage_donations"));
    }

    @Test
    @DisplayName("SECURITY 4.2: Treasurer CANNOT update Mosque A (lack of MOSQUE_UPDATE - 403 Forbidden)")
    void treasurerCannotUpdateMosque() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenTreasurerA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("SECURITY 4.3: Volunteer Coordinator CANNOT manage donations on Mosque A (403 Forbidden)")
    void volunteerCoordCannotManageDonations() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId + "/donations")
                        .header("Authorization", "Bearer " + tokenVolunteerCoordA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("SECURITY 4.4: Volunteer Coordinator CAN manage volunteers on Mosque A (ALLOW)")
    void volunteerCoordCanManageVolunteers() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId + "/volunteers")
                        .header("Authorization", "Bearer " + tokenVolunteerCoordA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action").value("manage_volunteers"));
    }

    // --- Scenario 5: Global Role Boundary ---

    @Test
    @DisplayName("SECURITY 5.1: Standard user can create global initiative (ALLOW via ROLE_USER)")
    void standardUserCanCreateInitiative() throws Exception {
        mockMvc.perform(post("/api/v1/test/authz/global/initiatives")
                        .header("Authorization", "Bearer " + tokenStandardUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action").value("create_initiative"));
    }

    @Test
    @DisplayName("SECURITY 5.2: Standard user CANNOT update Mosque A without membership (403 Forbidden)")
    void standardUserCannotUpdateMosqueWithoutMembership() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenStandardUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("SECURITY 5.3: Platform Super Admin retains global bypass (ALLOW on any resource)")
    void adminCanManageAnyResource() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueBId)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/v1/test/authz/projects/" + projectAId)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // --- Scenario 6: Account Status ---

    @Test
    @DisplayName("SECURITY 6.1: Suspended user token is rejected (401 Unauthorized)")
    void suspendedUserRejected() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenSuspended)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("SECURITY 6.2: Inactive user token is rejected (401 Unauthorized)")
    void inactiveUserRejected() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .header("Authorization", "Bearer " + tokenInactive)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // --- Scenario 7: Authentication Boundary ---

    @Test
    @DisplayName("SECURITY 7.1: Unauthenticated request returns 401 Unauthorized")
    void unauthenticatedReturns401() throws Exception {
        mockMvc.perform(put("/api/v1/test/authz/mosques/" + mosqueAId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
