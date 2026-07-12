package com.mohsinon.modules.authorization.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.mohsinon.common.api.ApiMessage;
import com.mohsinon.common.api.ApiResponse;
import com.mohsinon.common.api.ApiConstants;
import com.mohsinon.common.api.ApiResponseBuilder;
import com.mohsinon.modules.authorization.dto.PermissionGroupRequest;
import com.mohsinon.modules.authorization.dto.PermissionGroupResponse;
import com.mohsinon.modules.authorization.service.PermissionGroupService;

@RestController
@RequestMapping(ApiConstants.API_V1 + "/permission-groups")
@RequiredArgsConstructor
public class PermissionGroupController {

    private final PermissionGroupService service;

    @GetMapping
    public ApiResponse<List<PermissionGroupResponse>> findAll() {

        return ApiResponseBuilder.success(
                ApiMessage.FETCHED,
                service.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionGroupResponse> findById(
            @PathVariable Long id) {

        return ApiResponseBuilder.success(
                ApiMessage.FETCHED,
                service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PermissionGroupResponse> create(
            @Valid @RequestBody PermissionGroupRequest request) {

        return ApiResponseBuilder.success(
                ApiMessage.CREATED,
                service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<PermissionGroupResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PermissionGroupRequest request) {

        return ApiResponseBuilder.success(
                ApiMessage.UPDATED,
                service.update(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {

        service.delete(id);

        return ApiResponseBuilder.deleted();
    }

}