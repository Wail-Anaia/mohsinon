package com.mohsinon.core.security.authorization.service;

import com.mohsinon.core.exception.ForbiddenException;
import com.mohsinon.core.security.authorization.entity.Membership;
import com.mohsinon.core.security.authorization.model.GlobalRoleType;
import com.mohsinon.core.security.authorization.model.PermissionRegistry;
import com.mohsinon.core.security.authorization.model.PermissionType;
import com.mohsinon.core.security.authorization.model.ResourceContext;
import com.mohsinon.core.security.authorization.repository.MembershipRepository;
import com.mohsinon.core.security.authorization.repository.UserGlobalRoleRepository;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserStatus;
import com.mohsinon.modules.identity.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class DefaultAuthorizationService implements AuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuthorizationService.class);

    private final UserRepository userRepository;
    private final UserGlobalRoleRepository userGlobalRoleRepository;
    private final MembershipRepository membershipRepository;

    public DefaultAuthorizationService(UserRepository userRepository,
                                       UserGlobalRoleRepository userGlobalRoleRepository,
                                       MembershipRepository membershipRepository) {
        this.userRepository = userRepository;
        this.userGlobalRoleRepository = userGlobalRoleRepository;
        this.membershipRepository = membershipRepository;
    }

    @Override
    public boolean hasGlobalPermission(UUID userId, PermissionType permission) {
        return evaluatePermission(userId, permission, null);
    }

    @Override
    public boolean hasPermission(UUID userId, PermissionType permission, ResourceContext context) {
        return evaluatePermission(userId, permission, context);
    }

    @Override
    public boolean hasPermission(UUID userId, PermissionType permission, String resourceType, UUID resourceId) {
        if (resourceType == null || resourceId == null) {
            return evaluatePermission(userId, permission, null);
        }
        return evaluatePermission(userId, permission, ResourceContext.of(resourceType, resourceId));
    }

    @Override
    public boolean canManageMosque(UUID userId, UUID mosqueId) {
        if (mosqueId == null) {
            return false;
        }
        return evaluatePermission(userId, PermissionType.MOSQUE_UPDATE, ResourceContext.mosque(mosqueId));
    }

    @Override
    public boolean isAdmin(UUID userId) {
        if (userId == null) {
            return false;
        }
        if (!isUserActive(userId)) {
            return false;
        }
        return userGlobalRoleRepository.userHasRoleName(userId, GlobalRoleType.ROLE_ADMIN.name());
    }

    @Override
    public void requireGlobalPermission(UUID userId, PermissionType permission) {
        if (!hasGlobalPermission(userId, permission)) {
            log.warn("Access DENIED: User {} lacks global permission {}", userId, permission);
            throw new ForbiddenException("INSUFFICIENT_PERMISSIONS",
                    String.format("You do not have the required global permission: %s", permission));
        }
    }

    @Override
    public void requirePermission(UUID userId, PermissionType permission, ResourceContext context) {
        if (!hasPermission(userId, permission, context)) {
            log.warn("Access DENIED: User {} lacks permission {} on context {}", userId, permission, context);
            throw new ForbiddenException("INSUFFICIENT_CONTEXTUAL_PERMISSIONS",
                    String.format("You do not have the required permission %s on %s %s",
                            permission,
                            context != null ? context.resourceType() : "GLOBAL",
                            context != null ? context.resourceId() : ""));
        }
    }

    @Override
    public void requirePermission(UUID userId, PermissionType permission, String resourceType, UUID resourceId) {
        requirePermission(userId, permission, ResourceContext.of(resourceType, resourceId));
    }

    /**
     * Core 6-step Deny-by-Default decision pipeline.
     */
    private boolean evaluatePermission(UUID userId, PermissionType permission, ResourceContext context) {
        if (userId == null || permission == null) {
            return false;
        }

        // Step 1: User active check
        if (!isUserActive(userId)) {
            log.debug("Authz check failed: User {} is missing or not ACTIVE", userId);
            return false;
        }

        // Step 2: Global admin check
        Set<String> roleNames = userGlobalRoleRepository.findRoleNamesByUserId(userId);
        if (roleNames.contains(GlobalRoleType.ROLE_ADMIN.name())) {
            log.debug("Authz check passed via ROLE_ADMIN bypass for user {}", userId);
            return true;
        }

        // Step 3: Global role permission check
        for (String roleName : roleNames) {
            try {
                GlobalRoleType roleType = GlobalRoleType.valueOf(roleName);
                if (PermissionRegistry.doesGlobalRoleGrant(roleType, permission)) {
                    log.debug("Authz check passed: User {} has global permission {} via role {}", userId, permission, roleType);
                    return true;
                }
            } catch (IllegalArgumentException ignored) {
                // Unknown role string, skip safely
            }
        }

        // Step 4: Context presence check
        if (context == null) {
            log.debug("Authz check denied: Permission {} requires resource context for user {}", permission, userId);
            return false;
        }

        // Step 5: Contextual membership check
        List<Membership> memberships = membershipRepository.findEffectiveMemberships(
                userId,
                context.resourceType(),
                context.resourceId(),
                Instant.now()
        );

        for (Membership membership : memberships) {
            if (membership.isEffective() &&
                    PermissionRegistry.doesMembershipRoleGrant(membership.getMembershipRole(), permission)) {
                log.debug("Authz check passed: User {} has permission {} on {} via position {}",
                        userId, permission, context, membership.getMembershipRole());
                return true;
            }
        }

        // Step 6: Deny by default
        log.debug("Authz check denied: User {} lacks permission {} on context {}", userId, permission, context);
        return false;
    }

    private boolean isUserActive(UUID userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.isPresent() && userOpt.get().getStatus() == UserStatus.ACTIVE;
    }
}
