package com.mohsinon.modules.authorization.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.mohsinon.modules.authorization.dto.PermissionGroupRequest;
import com.mohsinon.modules.authorization.dto.PermissionGroupResponse;
import com.mohsinon.modules.authorization.entity.PermissionGroup;
import com.mohsinon.modules.authorization.exception.DuplicatePermissionGroupException;
import com.mohsinon.modules.authorization.exception.PermissionGroupNotFoundException;
import com.mohsinon.modules.authorization.mapper.PermissionGroupMapper;
import com.mohsinon.modules.authorization.repository.PermissionGroupRepository;
import com.mohsinon.modules.authorization.service.PermissionGroupService;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionGroupServiceImpl implements PermissionGroupService {

    private final PermissionGroupRepository repository;
    private final PermissionGroupMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<PermissionGroupResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionGroupResponse findById(Long id) {

        PermissionGroup group = repository.findById(id)
                .orElseThrow(() ->
                        new PermissionGroupNotFoundException(id));

        return mapper.toResponse(group);
    }

    @Override
    public PermissionGroupResponse create(PermissionGroupRequest request) {

        if (repository.existsByName(request.getName())) {
            throw new DuplicatePermissionGroupException(request.getCode());
        }
        
        if (repository.existsByCode(request.getCode())) {
            throw new DuplicatePermissionGroupException(request.getCode());
        }

        PermissionGroup group = new PermissionGroup();
        
        group.setCode(request.getCode());
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setActive(request.getActive());

        repository.save(group);

        return mapper.toResponse(group);
    }

    @Override
    public PermissionGroupResponse update(Long id,
                                          PermissionGroupRequest request) {

        PermissionGroup group = repository.findById(id)
                .orElseThrow(() ->
                        new PermissionGroupNotFoundException(id));
        
        group.setCode(request.getCode());
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setActive(request.getActive());

        repository.save(group);

        return mapper.toResponse(group);
    }

    @Override
    public void delete(Long id) {

        PermissionGroup group = repository.findById(id)
                .orElseThrow(() ->
                        new PermissionGroupNotFoundException(id));

        repository.delete(group);
    }
    
    @Override
    public PermissionGroupResponse getByCode(String code) {

        PermissionGroup group =
                repository.findByCode(code)
                        .orElseThrow(() ->
                                new PermissionGroupNotFoundException());

        return mapper.toResponse(group);
    }

}