package com.mohsinon.core.security.authorization.service;

import com.mohsinon.core.exception.ResourceNotFoundException;
import com.mohsinon.core.security.authorization.entity.Membership;
import com.mohsinon.core.security.authorization.model.MembershipRole;
import com.mohsinon.core.security.authorization.model.MembershipStatus;
import com.mohsinon.core.security.authorization.model.ResourceContext;
import com.mohsinon.core.security.authorization.repository.MembershipRepository;
import com.mohsinon.modules.identity.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MembershipService {

    private static final Logger log = LoggerFactory.getLogger(MembershipService.class);

    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    public MembershipService(MembershipRepository membershipRepository, UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }

    /**
     * Assigns or reactivates a contextual membership role for a user on a specific resource.
     */
    public Membership assignMembership(UUID userId,
                                       String resourceType,
                                       UUID resourceId,
                                       MembershipRole role,
                                       Instant expiresAt,
                                       UUID assignedBy) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }
        if (resourceType == null || resourceType.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource type must not be null or empty.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("Resource ID must not be null.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Membership role must not be null.");
        }

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }

        Optional<Membership> existing = membershipRepository
                .findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(userId, resourceType.trim().toUpperCase(), resourceId, role);

        Membership membership;
        if (existing.isPresent()) {
            membership = existing.get();
            membership.setStatus(MembershipStatus.ACTIVE);
            membership.setExpiresAt(expiresAt);
            membership.setAssignedBy(assignedBy);
            log.info("Reactivated existing membership {} for user {} on {} {}", membership.getId(), userId, resourceType, resourceId);
        } else {
            membership = new Membership(userId, resourceType, resourceId, role, expiresAt, assignedBy);
            log.info("Created new membership for user {} on {} {} with role {}", userId, resourceType, resourceId, role);
        }

        return membershipRepository.save(membership);
    }

    public Membership assignMembership(UUID userId, ResourceContext context, MembershipRole role, Instant expiresAt, UUID assignedBy) {
        if (context == null) {
            throw new IllegalArgumentException("Resource context must not be null.");
        }
        return assignMembership(userId, context.resourceType(), context.resourceId(), role, expiresAt, assignedBy);
    }

    public Membership assignMembership(UUID userId, ResourceContext context, MembershipRole role, UUID assignedBy) {
        return assignMembership(userId, context, role, null, assignedBy);
    }

    public Membership assignMosqueMembership(UUID userId, UUID mosqueId, MembershipRole role, UUID assignedBy) {
        return assignMembership(userId, ResourceContext.TYPE_MOSQUE, mosqueId, role, null, assignedBy);
    }

    /**
     * Revokes a membership by its ID.
     */
    public void revokeMembership(UUID membershipId) {
        if (membershipId == null) {
            throw new IllegalArgumentException("Membership ID must not be null.");
        }
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership", membershipId));

        membership.revoke();
        membershipRepository.save(membership);
        log.info("Revoked membership {}", membershipId);
    }

    /**
     * Revokes a specific membership role for a user on a resource.
     */
    public void revokeMembership(UUID userId, String resourceType, UUID resourceId, MembershipRole role) {
        if (userId == null || resourceType == null || resourceId == null || role == null) {
            throw new IllegalArgumentException("Parameters must not be null.");
        }
        membershipRepository.findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(
                userId, resourceType.trim().toUpperCase(), resourceId, role)
                .ifPresent(m -> {
                    m.revoke();
                    membershipRepository.save(m);
                    log.info("Revoked role {} for user {} on {} {}", role, userId, resourceType, resourceId);
                });
    }

    public void revokeMembership(UUID userId, ResourceContext context, MembershipRole role) {
        if (context == null) {
            throw new IllegalArgumentException("Resource context must not be null.");
        }
        revokeMembership(userId, context.resourceType(), context.resourceId(), role);
    }

    @Transactional(readOnly = true)
    public List<Membership> getUserActiveMemberships(UUID userId) {
        if (userId == null) {
            return List.of();
        }
        return membershipRepository.findByUserIdAndStatus(userId, MembershipStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Membership> getResourceActiveMemberships(String resourceType, UUID resourceId) {
        if (resourceType == null || resourceId == null) {
            return List.of();
        }
        return membershipRepository.findByResourceTypeAndResourceIdAndStatus(resourceType.trim().toUpperCase(), resourceId, MembershipStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Membership> getEffectiveMemberships(UUID userId, ResourceContext context) {
        if (userId == null || context == null) {
            return List.of();
        }
        return membershipRepository.findEffectiveMemberships(userId, context.resourceType(), context.resourceId(), Instant.now());
    }
}
