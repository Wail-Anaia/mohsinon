package com.mohsinon.core.security.authorization.evaluator;

import com.mohsinon.core.security.UserPrincipal;
import com.mohsinon.core.security.authorization.model.PermissionType;
import com.mohsinon.core.security.authorization.service.AuthorizationService;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityAuthzEvaluatorTest {

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private SecurityAuthzEvaluator authzEvaluator;

    private UserPrincipal principal;
    private UUID userId;
    private UUID mosqueId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        mosqueId = UUID.randomUUID();

        User user = new User("imam_ahmed", "ahmed@example.com", "hash", "Ahmed", "Mansour", null);
        user.setId(userId);
        user.setStatus(UserStatus.ACTIVE);

        principal = UserPrincipal.fromUser(user, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    @DisplayName("Should delegate contextual permission check to AuthorizationService")
    void shouldDelegateContextualPermissionCheck() {
        when(authorizationService.hasPermission(userId, PermissionType.MOSQUE_UPDATE, "MOSQUE", mosqueId))
                .thenReturn(true);

        boolean allowed = authzEvaluator.hasPermission(principal, "MOSQUE_UPDATE", "MOSQUE", mosqueId);

        assertThat(allowed).isTrue();
        verify(authorizationService).hasPermission(userId, PermissionType.MOSQUE_UPDATE, "MOSQUE", mosqueId);
    }

    @Test
    @DisplayName("Should normalize permission string and delegate correctly")
    void shouldNormalizePermissionString() {
        when(authorizationService.hasPermission(userId, PermissionType.MOSQUE_UPDATE, "MOSQUE", mosqueId))
                .thenReturn(true);

        boolean allowed = authzEvaluator.hasPermission(principal, "  mosque_update  ", "MOSQUE", mosqueId);

        assertThat(allowed).isTrue();
        verify(authorizationService).hasPermission(userId, PermissionType.MOSQUE_UPDATE, "MOSQUE", mosqueId);
    }

    @Test
    @DisplayName("Should return false when principal or permission is null")
    void shouldReturnFalseForNullInputs() {
        assertThat(authzEvaluator.hasPermission(null, "MOSQUE_UPDATE", "MOSQUE", mosqueId)).isFalse();
        assertThat(authzEvaluator.hasPermission(principal, null, "MOSQUE", mosqueId)).isFalse();
        assertThat(authzEvaluator.hasGlobalPermission(null, "MOSQUE_VIEW")).isFalse();
        assertThat(authzEvaluator.hasGlobalPermission(principal, null)).isFalse();
        assertThat(authzEvaluator.canManageMosque(null, mosqueId)).isFalse();
        assertThat(authzEvaluator.canManageMosque(principal, null)).isFalse();
        assertThat(authzEvaluator.isAdmin(null)).isFalse();

        verifyNoInteractions(authorizationService);
    }

    @Test
    @DisplayName("Should return false for unknown/invalid permission string")
    void shouldReturnFalseForUnknownPermission() {
        boolean allowed = authzEvaluator.hasPermission(principal, "UNKNOWN_PERMISSION_123", "MOSQUE", mosqueId);

        assertThat(allowed).isFalse();
        verifyNoInteractions(authorizationService);
    }

    @Test
    @DisplayName("Should delegate global permission check to AuthorizationService")
    void shouldDelegateGlobalPermissionCheck() {
        when(authorizationService.hasGlobalPermission(userId, PermissionType.DONATION_CREATE))
                .thenReturn(true);

        boolean allowed = authzEvaluator.hasGlobalPermission(principal, "DONATION_CREATE");

        assertThat(allowed).isTrue();
        verify(authorizationService).hasGlobalPermission(userId, PermissionType.DONATION_CREATE);
    }

    @Test
    @DisplayName("Should delegate canManageMosque check to AuthorizationService")
    void shouldDelegateCanManageMosque() {
        when(authorizationService.canManageMosque(userId, mosqueId)).thenReturn(true);

        boolean allowed = authzEvaluator.canManageMosque(principal, mosqueId);

        assertThat(allowed).isTrue();
        verify(authorizationService).canManageMosque(userId, mosqueId);
    }

    @Test
    @DisplayName("Should delegate isAdmin check to AuthorizationService")
    void shouldDelegateIsAdmin() {
        when(authorizationService.isAdmin(userId)).thenReturn(true);

        boolean admin = authzEvaluator.isAdmin(principal);

        assertThat(admin).isTrue();
        verify(authorizationService).isAdmin(userId);
    }
}
