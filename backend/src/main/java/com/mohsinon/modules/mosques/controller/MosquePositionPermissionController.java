package com.mohsinon.modules.mosques.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mohsinon.common.api.ApiMessage;
import com.mohsinon.common.api.ApiResponse;
import com.mohsinon.common.api.ApiConstants;
import com.mohsinon.common.api.ApiResponseBuilder;
import com.mohsinon.modules.authorization.dto.PositionPermissionRequest;
import com.mohsinon.modules.authorization.dto.PositionPermissionResponse;
import com.mohsinon.modules.authorization.service.PositionPermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiConstants.API_V1 + "/positions")
@RequiredArgsConstructor
public class MosquePositionPermissionController {

    private final PositionPermissionService service;

    @GetMapping("/{positionId}/permissions")
    public ApiResponse<List<PositionPermissionResponse>> getPermissions(
            @PathVariable Long positionId) {

        return ApiResponseBuilder.success(
                ApiMessage.FETCHED,
                service.getPermissions(positionId));
    }

    @PostMapping("/{positionId}/permissions")
    public ResponseEntity<ApiResponse<PositionPermissionResponse>> assignPermission(
            @PathVariable Long positionId,
            @Valid @RequestBody PositionPermissionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseBuilder.success(
                        ApiMessage.CREATED,
                        service.assignPermission(positionId, request)));
    }

    @DeleteMapping("/{positionId}/permissions/{permissionId}")
    public ApiResponse<Void> removePermission(
            @PathVariable Long positionId,
            @PathVariable Long permissionId) {

        service.removePermission(positionId, permissionId);

        return ApiResponseBuilder.success(ApiMessage.DELETED);
    }

}