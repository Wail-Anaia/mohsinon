package com.mohsinon.modules.authorization.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.mohsinon.modules.authorization.dto.request.PermissionRequest;
import com.mohsinon.modules.authorization.dto.response.PermissionResponse;
import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PermissionGroup;
import com.mohsinon.modules.authorization.exception.DuplicatePermissionException;
import com.mohsinon.modules.authorization.exception.PermissionNotFoundException;
import com.mohsinon.modules.authorization.mapper.PermissionMapper;
import com.mohsinon.modules.authorization.repository.PermissionGroupRepository;
import com.mohsinon.modules.authorization.repository.PermissionRepository;
import com.mohsinon.modules.authorization.service.PermissionService;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionGroupRepository permissionGroupRepository;
    private final PermissionMapper permissionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> findAll() {

        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionResponse findById(Long id) {

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException(id));

        return permissionMapper.toResponse(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> findByGroup(String groupCode) {

        return permissionRepository.findByPermissionGroup_Code(groupCode)
                .stream()
                .map(permissionMapper::toResponse)
                .toList();
    }

    @Override
    public PermissionResponse create(PermissionRequest request) {

        if (permissionRepository.existsByCode(request.getCode())) {
            throw new DuplicatePermissionException(request.getCode());
        }

        PermissionGroup permissionGroup = permissionGroupRepository
                .findById(request.getPermissionGroupId())
                .orElseThrow(() ->
                        new com.mohsinon.modules.authorization.exception.PermissionGroupNotFoundException(
                                request.getPermissionGroupId()));

        Permission permission = new Permission();

        permission.setCode(request.getCode());
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permission.setPermissionGroup(permissionGroup);
        permission.setActive(request.getActive());

        permissionRepository.save(permission);

        return permissionMapper.toResponse(permission);
    }

    @Override
    public PermissionResponse update(Long id, PermissionRequest request) {

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException(id));

        PermissionGroup permissionGroup = permissionGroupRepository
                .findById(request.getPermissionGroupId())
                .orElseThrow(() ->
                        new com.mohsinon.modules.authorization.exception.PermissionGroupNotFoundException(
                                request.getPermissionGroupId()));

        permission.setCode(request.getCode());
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permission.setPermissionGroup(permissionGroup);
        permission.setActive(request.getActive());

        permissionRepository.save(permission);

        return permissionMapper.toResponse(permission);
    }

    @Override
    public void delete(Long id) {

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException(id));

        permissionRepository.delete(permission);
    }

}