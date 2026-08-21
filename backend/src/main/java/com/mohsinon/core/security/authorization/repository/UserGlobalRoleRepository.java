package com.mohsinon.core.security.authorization.repository;

import com.mohsinon.core.security.authorization.entity.UserGlobalRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserGlobalRoleRepository extends JpaRepository<UserGlobalRole, UUID> {

    List<UserGlobalRole> findByUserId(UUID userId);

    Optional<UserGlobalRole> findByUserIdAndRoleId(UUID userId, UUID roleId);

    boolean existsByUserIdAndRoleId(UUID userId, UUID roleId);

    void deleteByUserIdAndRoleId(UUID userId, UUID roleId);

    void deleteByUserId(UUID userId);

    @Query("SELECT r.name FROM GlobalRole r, UserGlobalRole ugr WHERE r.id = ugr.roleId AND ugr.userId = :userId")
    Set<String> findRoleNamesByUserId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(ugr) > 0 FROM GlobalRole r, UserGlobalRole ugr WHERE r.id = ugr.roleId AND ugr.userId = :userId AND r.name = :roleName")
    boolean userHasRoleName(@Param("userId") UUID userId, @Param("roleName") String roleName);
}
