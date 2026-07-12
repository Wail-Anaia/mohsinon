package com.mohsinon.modules.authorization.service;

import java.util.List;

import com.mohsinon.modules.authorization.dto.PermissionRequest;
import com.mohsinon.modules.authorization.dto.PermissionResponse;

public interface PermissionService {

    List<PermissionResponse> findAll();

    PermissionResponse findById(Long id);

    List<PermissionResponse> findByGroup(String groupCode);

    PermissionResponse create(PermissionRequest request);

    PermissionResponse update(Long id, PermissionRequest request);

    void delete(Long id);

}