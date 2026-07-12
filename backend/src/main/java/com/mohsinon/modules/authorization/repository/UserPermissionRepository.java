package com.mohsinon.modules.authorization.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.UserPermission;
import com.mohsinon.modules.users.entity.User;

public interface UserPermissionRepository
        extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByUser(User user);

    List<UserPermission> findByUserId(UUID userId);

    boolean existsByUserAndPermission(
            User user,
            Permission permission);

    Optional<UserPermission> findByUserAndPermission(
            User user,
            Permission permission);

    void deleteByUserAndPermission(
            User user,
            Permission permission);

}