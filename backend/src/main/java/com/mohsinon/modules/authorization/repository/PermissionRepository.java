package com.mohsinon.modules.authorization.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mohsinon.modules.authorization.entity.Permission;

public interface PermissionRepository
        extends JpaRepository<Permission, Long> {

    Optional<Permission> findByCode(String code);

    boolean existsByCode(String code);

    List<Permission> findByPermissionGroup_Code(String code);

}