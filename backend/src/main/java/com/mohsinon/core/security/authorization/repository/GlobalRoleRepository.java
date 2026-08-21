package com.mohsinon.core.security.authorization.repository;

import com.mohsinon.core.security.authorization.entity.GlobalRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GlobalRoleRepository extends JpaRepository<GlobalRole, UUID> {

    Optional<GlobalRole> findByName(String name);
}
