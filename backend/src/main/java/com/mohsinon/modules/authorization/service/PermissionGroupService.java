package com.mohsinon.modules.authorization.service;

import java.util.List;

import com.mohsinon.modules.authorization.dto.PermissionGroupRequest;
import com.mohsinon.modules.authorization.dto.PermissionGroupResponse;

public interface PermissionGroupService {

    List<PermissionGroupResponse> findAll();

    PermissionGroupResponse findById(Long id);

    PermissionGroupResponse create(PermissionGroupRequest request);

    PermissionGroupResponse update(Long id, PermissionGroupRequest request);

    void delete(Long id);

}