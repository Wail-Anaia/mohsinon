package com.mohsinon.modules.authorization.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mohsinon.common.api.ApiMessage;
import com.mohsinon.common.api.ApiResponse;
import com.mohsinon.common.api.ApiConstants;
import com.mohsinon.common.api.ApiResponseBuilder;
import com.mohsinon.modules.authorization.dto.request.PermissionRequest;
import com.mohsinon.modules.authorization.dto.response.PermissionResponse;
import com.mohsinon.modules.authorization.service.PermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiConstants.API_V1 + "/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ApiResponse<List<PermissionResponse>> findAll() {

        return ApiResponseBuilder.success(
                ApiMessage.FETCHED,
                permissionService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionResponse> findById(
            @PathVariable Long id) {

        return ApiResponseBuilder.success(
                ApiMessage.FETCHED,
                permissionService.findById(id));
    }

    @GetMapping("/group/{groupCode}")
    public ApiResponse<List<PermissionResponse>> findByGroup(
            @PathVariable String groupCode) {

        return ApiResponseBuilder.success(
                ApiMessage.FETCHED,
                permissionService.findByGroup(groupCode));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> create(
            @Valid @RequestBody PermissionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseBuilder.success(
                        ApiMessage.CREATED,
                        permissionService.create(request)));
    }

    @PutMapping("/{id}")
    public ApiResponse<PermissionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PermissionRequest request) {

        return ApiResponseBuilder.success(
                ApiMessage.UPDATED,
                permissionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id) {

        permissionService.delete(id);

        return ApiResponseBuilder.success(ApiMessage.DELETED);
    }

}