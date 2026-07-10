package com.mohsinon.modules.authorization.repository;

import com.mohsinon.modules.authorization.entity.UserPermission;
import com.mohsinon.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPermissionRepository
        extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByUser(User user);

}