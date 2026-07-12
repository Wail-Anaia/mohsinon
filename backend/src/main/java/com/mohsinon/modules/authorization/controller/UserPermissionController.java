package com.mohsinon.modules.authorization.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.mohsinon.common.api.ApiMessage;
import com.mohsinon.common.api.ApiResponse;
import com.mohsinon.common.api.ApiResponseBuilder;
import com.mohsinon.common.api.ApiConstants;
import com.mohsinon.modules.authorization.dto.UserPermissionRequest;
import com.mohsinon.modules.authorization.dto.UserPermissionResponse;
import com.mohsinon.modules.authorization.service.UserPermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.API_V1 + "/users/{userId}/permissions")
public class UserPermissionController {

    private final UserPermissionService service;

    @GetMapping
    public ApiResponse<List<UserPermissionResponse>> findByUser(
            @PathVariable UUID userId) {

        return ApiResponseBuilder.success(
                ApiMessage.FETCHED,
                service.findByUser(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserPermissionResponse> assignPermission(
            @PathVariable UUID userId,
            @Valid @RequestBody UserPermissionRequest request) {

        return ApiResponseBuilder.success(
                ApiMessage.CREATED,
                service.assignPermission(userId, request));
    }

    @DeleteMapping("/{permissionId}")
    public ApiResponse<Void> removePermission(
            @PathVariable UUID userId,
            @PathVariable Long permissionId) {

        service.removePermission(userId, permissionId);

        return ApiResponseBuilder.success(
                ApiMessage.DELETED);
    }

}