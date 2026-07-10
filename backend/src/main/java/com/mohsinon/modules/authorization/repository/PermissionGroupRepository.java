package com.mohsinon.modules.authorization.repository;

import com.mohsinon.modules.authorization.entity.PermissionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Long> {

    Optional<PermissionGroup> findByCode(String code);

    boolean existsByCode(String code);

}