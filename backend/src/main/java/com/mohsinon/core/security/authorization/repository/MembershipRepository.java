package com.mohsinon.core.security.authorization.repository;

import com.mohsinon.core.security.authorization.entity.Membership;
import com.mohsinon.core.security.authorization.model.MembershipRole;
import com.mohsinon.core.security.authorization.model.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {

    List<Membership> findByUserIdAndStatus(UUID userId, MembershipStatus status);

    List<Membership> findByUserIdAndResourceTypeAndResourceIdAndStatus(
            UUID userId, String resourceType, UUID resourceId, MembershipStatus status);

    List<Membership> findByResourceTypeAndResourceIdAndStatus(
            String resourceType, UUID resourceId, MembershipStatus status);

    Optional<Membership> findByUserIdAndResourceTypeAndResourceIdAndMembershipRole(
            UUID userId, String resourceType, UUID resourceId, MembershipRole membershipRole);

    boolean existsByUserIdAndResourceTypeAndResourceIdAndMembershipRoleAndStatus(
            UUID userId, String resourceType, UUID resourceId, MembershipRole membershipRole, MembershipStatus status);

    @Query("SELECT m FROM Membership m " +
            "WHERE m.userId = :userId " +
            "AND m.resourceType = :resourceType " +
            "AND m.resourceId = :resourceId " +
            "AND m.status = 'ACTIVE' " +
            "AND (m.expiresAt IS NULL OR m.expiresAt > :now)")
    List<Membership> findEffectiveMemberships(
            @Param("userId") UUID userId,
            @Param("resourceType") String resourceType,
            @Param("resourceId") UUID resourceId,
            @Param("now") Instant now);
}
