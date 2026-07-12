package com.mohsinon.modules.authorization.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.mohsinon.modules.authorization.dto.PositionPermissionRequest;
import com.mohsinon.modules.authorization.dto.PositionPermissionResponse;
import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PositionPermission;
import com.mohsinon.modules.authorization.exception.DuplicatePositionPermissionException;
import com.mohsinon.modules.authorization.exception.PermissionNotFoundException;
import com.mohsinon.modules.authorization.exception.PositionPermissionNotFoundException;
import com.mohsinon.modules.authorization.mapper.PositionPermissionMapper;
import com.mohsinon.modules.authorization.repository.PermissionRepository;
import com.mohsinon.modules.authorization.repository.PositionPermissionRepository;
import com.mohsinon.modules.authorization.service.PositionPermissionService;
import com.mohsinon.modules.mosques.api.MosqueApi;
import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.authorization.cache.PermissionCache;

@Service
@RequiredArgsConstructor
@Transactional
public class PositionPermissionServiceImpl
        implements PositionPermissionService {

    private final PositionPermissionRepository repository;

    private final PermissionRepository permissionRepository;

    private final PositionPermissionMapper mapper;

    private final MosqueApi mosqueApi;
    
    private final PermissionCache permissionCache;

    @Override
    @Transactional(readOnly = true)
    public List<PositionPermissionResponse> getPermissions(
            Long positionId) {

        MosquePosition position =
                mosqueApi.getPositionById(positionId);

        return repository.findByPosition(position)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public PositionPermissionResponse assignPermission(
            Long positionId,
            PositionPermissionRequest request) {

        MosquePosition position =
                mosqueApi.getPositionById(positionId);

        Permission permission =
                permissionRepository.findById(request.getPermissionId())
                        .orElseThrow(() ->
                                new PermissionNotFoundException(
                                        request.getPermissionId()));

        if (repository.existsByPositionAndPermission(position, permission)) {
            throw new DuplicatePositionPermissionException();
        }

        PositionPermission entity = new PositionPermission();

        entity.setPosition(position);
        entity.setPermission(permission);

        repository.save(entity);
        
        permissionCache.clear();

        return mapper.toResponse(entity);
    }

    @Override
    public void removePermission(
            Long positionId,
            Long permissionId) {

        MosquePosition position =
                mosqueApi.getPositionById(positionId);

        Permission permission =
                permissionRepository.findById(permissionId)
                        .orElseThrow(() ->
                                new PermissionNotFoundException(permissionId));

        PositionPermission entity =
                repository.findByPositionAndPermission(position, permission)
                        .orElseThrow(PositionPermissionNotFoundException::new);

        repository.delete(entity);
        
        permissionCache.clear();
    }

}