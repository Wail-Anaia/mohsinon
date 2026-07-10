package com.mohsinon.modules.users.repository;

import com.mohsinon.modules.users.entity.Role;
import com.mohsinon.modules.users.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}