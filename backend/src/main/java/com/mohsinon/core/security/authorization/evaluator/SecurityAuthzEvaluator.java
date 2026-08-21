package com.mohsinon.core.security.authorization.evaluator;

import com.mohsinon.core.security.UserPrincipal;
import com.mohsinon.core.security.authorization.model.PermissionType;
import com.mohsinon.core.security.authorization.service.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

/**
 * Spring Security SpEL Evaluator bean exposed as {@code @authz}.
 * Keeps SpEL expressions thin by delegating all business evaluation to AuthorizationService.
 *
 * Example usage in controllers:
 * <pre>
 * &#64;PreAuthorize("@authz.hasPermission(principal, 'MOSQUE_UPDATE', 'MOSQUE', #mosqueId)")
 * &#64;PreAuthorize("@authz.canManageMosque(principal, #mosqueId)")
 * &#64;PreAuthorize("@authz.hasGlobalPermission(principal, 'DONATION_CREATE')")
 * </pre>
 */
@Component("authz")
public class SecurityAuthzEvaluator {

    private static final Logger log = LoggerFactory.getLogger(SecurityAuthzEvaluator.class);

    private final AuthorizationService authorizationService;

    public SecurityAuthzEvaluator(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public boolean hasPermission(UserPrincipal principal, String permission, String resourceType, UUID resourceId) {
        if (principal == null || permission == null) {
            return false;
        }

        PermissionType permissionType = parsePermissionType(permission);
        if (permissionType == null) {
            log.warn("Unknown permission requested in @authz check: {}", permission);
            return false;
        }

        return authorizationService.hasPermission(principal.getId(), permissionType, resourceType, resourceId);
    }

    public boolean hasGlobalPermission(UserPrincipal principal, String permission) {
        if (principal == null || permission == null) {
            return false;
        }

        PermissionType permissionType = parsePermissionType(permission);
        if (permissionType == null) {
            log.warn("Unknown global permission requested in @authz check: {}", permission);
            return false;
        }

        return authorizationService.hasGlobalPermission(principal.getId(), permissionType);
    }

    public boolean canManageMosque(UserPrincipal principal, UUID mosqueId) {
        if (principal == null || mosqueId == null) {
            return false;
        }
        return authorizationService.canManageMosque(principal.getId(), mosqueId);
    }

    public boolean isAdmin(UserPrincipal principal) {
        if (principal == null) {
            return false;
        }
        return authorizationService.isAdmin(principal.getId());
    }

    private PermissionType parsePermissionType(String permission) {
        try {
            return PermissionType.valueOf(permission.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
