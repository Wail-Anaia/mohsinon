package com.mohsinon.modules.authorization.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mohsinon.modules.authorization.dto.request.UserPermissionRequest;
import com.mohsinon.modules.authorization.dto.response.UserPermissionResponse;
import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.UserPermission;
import com.mohsinon.modules.authorization.exception.DuplicateUserPermissionException;
import com.mohsinon.modules.authorization.exception.PermissionNotFoundException;
import com.mohsinon.modules.authorization.exception.UserPermissionNotFoundException;
import com.mohsinon.modules.authorization.mapper.UserPermissionMapper;
import com.mohsinon.modules.authorization.repository.PermissionRepository;
import com.mohsinon.modules.authorization.repository.UserPermissionRepository;
import com.mohsinon.modules.authorization.service.UserPermissionService;
import com.mohsinon.modules.users.api.UserApi;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.modules.authorization.cache.PermissionCache;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPermissionServiceImpl
        implements UserPermissionService {

    private final UserApi userApi;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository repository;
    private final UserPermissionMapper mapper;
    private final PermissionCache permissionCache;
    
    @Override
    @Transactional(readOnly = true)
    public List<UserPermissionResponse> findByUser(UUID userId) {

        User user = userApi.getUserById(userId);

        return repository.findByUser(user)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public UserPermissionResponse assignPermission(
            UUID userId,
            UserPermissionRequest request) {

        User user = userApi.getUserById(userId);

        Permission permission = permissionRepository
                .findById(request.getPermissionId())
                .orElseThrow(() ->
                        new PermissionNotFoundException(
                                request.getPermissionId()));

        if (repository.existsByUserAndPermission(user, permission)) {
            throw new DuplicateUserPermissionException();
        }

        UserPermission entity = new UserPermission();

        entity.setUser(user);
        entity.setPermission(permission);
        entity.setGranted(
                request.getGranted() == null
                        ? true
                        : request.getGranted());

        entity = repository.save(entity);
        
        permissionCache.evict(userId); 

        return mapper.toResponse(entity);
    }

    @Override
    public void removePermission(
            UUID userId,
            Long permissionId) {

        User user = userApi.getUserById(userId);

        Permission permission = permissionRepository
                .findById(permissionId)
                .orElseThrow(() ->
                        new PermissionNotFoundException(permissionId));

        UserPermission entity = repository
                .findByUserAndPermission(user, permission)
                .orElseThrow(UserPermissionNotFoundException::new);

        repository.delete(entity);
        
        permissionCache.evict(userId);
    }

}