package com.mohsinon.integration.authorization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import com.mohsinon.modules.authorization.dto.PermissionGroupRequest;
import com.mohsinon.modules.authorization.dto.PermissionGroupResponse;
import com.mohsinon.modules.authorization.dto.PermissionRequest;
import com.mohsinon.modules.authorization.dto.PermissionResponse;
import com.mohsinon.modules.authorization.dto.PositionPermissionRequest;
import com.mohsinon.modules.authorization.service.AuthorizationService;
import com.mohsinon.modules.authorization.service.PermissionGroupService;
import com.mohsinon.modules.authorization.service.PermissionService;
import com.mohsinon.modules.authorization.service.PositionPermissionService;
import com.mohsinon.modules.authorization.service.UserPermissionService;
import com.mohsinon.modules.mosques.api.MosqueApi;
import com.mohsinon.modules.mosques.constants.MosquePositionCodes;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.mosques.repository.MosqueMembershipRepository;
import com.mohsinon.modules.mosques.repository.MosquePositionRepository;
import com.mohsinon.modules.mosques.repository.MosqueRepository;
import com.mohsinon.modules.users.api.UserApi;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.modules.users.repository.UserRepository;


@SpringBootTest
@Transactional
class AuthorizationIntegrationTest {

    @Autowired
    private PermissionGroupService permissionGroupService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PositionPermissionService positionPermissionService;

    @Autowired
    private MosqueApi mosqueApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private AuthorizationService authorizationService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MosqueRepository mosqueRepository;

    @Autowired
    private MosqueMembershipRepository membershipRepository;

    @Autowired
    private MosquePositionRepository positionRepository;
    
    private Long permissionGroupId;

    private Long permissionId;

    private Long positionId;

    private UUID userId;

    private UUID mosqueId;
    
    @BeforeEach
    void setup() {

        createPermissionGroup();

        createPermission();

        loadDefaultPosition();
        
        assignPermissionToImam();

    }
    
    private void createPermissionGroup() {

        PermissionGroupRequest request =
                new PermissionGroupRequest();

        request.setCode("TEST_GROUP");
        request.setName("Test Group");
        request.setDescription("Integration Test");

        PermissionGroupResponse response =
                permissionGroupService.create(request);

        permissionGroupId = response.getId();
    }
    
    private void createPermission() {

        PermissionRequest request =
                new PermissionRequest();

        request.setPermissionGroupId(permissionGroupId);
        request.setCode("test.permission");
        request.setName("Test Permission");
        request.setDescription("Integration Test");

        PermissionResponse response =
                permissionService.create(request);

        permissionId = response.getId();
    }
    
    private void loadDefaultPosition() {

        positionId =
                mosqueApi
                        .getPositionByCode("IMAM")
                        .getId();
    }
    
    private User createUser() {

        User user = new User();

        user.setFirstName("Integration");
        user.setLastName("Test");
        user.setUsername("integration-user");
        user.setEmail("integration@test.com");
        user.setPassword("123456");

        return userRepository.save(user);
    }
    
    private Mosque createMosque() {

        Mosque mosque = new Mosque();

        mosque.setName("Test Mosque");
        mosque.setCountry("Morocco");
        mosque.setCity("Meknes");
        mosque.setActive(true);

        return mosqueRepository.save(mosque);
    }
    
    private MosqueMembership createMembership(
            User user,
            Mosque mosque) {

        MosquePosition imam =
                positionRepository
                        .findByCode(MosquePositionCodes.IMAM)
                        .orElseThrow();

        MosqueMembership membership =
                new MosqueMembership();

        membership.setUser(user);
        membership.setMosque(mosque);
        membership.setPosition(imam);
        membership.activate();

        return membershipRepository.save(membership);
    }
    
    private void assignPermissionToImam() {

        PositionPermissionRequest request =
                new PositionPermissionRequest();

        request.setPermissionId(permissionId);

        positionPermissionService.assignPermission(
                positionId,
                request);
    }
    
    @Test
    void shouldAuthorizeImam() {

        User user = createUser();

        Mosque mosque = createMosque();

        createMembership(user, mosque);

        assertDoesNotThrow(() ->

                authorizationService.checkPermission(
                        user,
                        "mosque",
                        mosque.getId(),
                        "test.permission")
        );
    }

}