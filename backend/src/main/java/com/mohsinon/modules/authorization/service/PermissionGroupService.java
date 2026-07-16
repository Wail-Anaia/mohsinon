package com.mohsinon.modules.authorization.service;

import java.util.List;

import com.mohsinon.modules.authorization.dto.request.PermissionGroupRequest;
import com.mohsinon.modules.authorization.dto.response.PermissionGroupResponse;

public interface PermissionGroupService {

    List<PermissionGroupResponse> findAll();

    PermissionGroupResponse findById(Long id);
    
    PermissionGroupResponse getByCode(String code);

    PermissionGroupResponse create(PermissionGroupRequest request);

    PermissionGroupResponse update(Long id, PermissionGroupRequest request);

    void delete(Long id);

}