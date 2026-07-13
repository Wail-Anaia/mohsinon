package com.mohsinon.modules.donations.controller;

import com.mohsinon.modules.donations.dto.request.CreateDonationCategoryRequest;
import com.mohsinon.modules.donations.dto.request.UpdateDonationCategoryRequest;
import com.mohsinon.modules.donations.dto.response.DonationCategoryResponse;
import com.mohsinon.modules.donations.service.DonationCategoryService;
import com.mohsinon.common.api.ApiConstants;
import com.mohsinon.security.annotation.RequirePermission;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_V1 + "/donation-categories")
public class DonationCategoryController {

    private final DonationCategoryService service;

    public DonationCategoryController(DonationCategoryService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequirePermission(groupCode = "donation", permission = "category.create")
    public DonationCategoryResponse create(
            @Valid @RequestBody CreateDonationCategoryRequest request) {

        return service.create(request);
    }

    @PutMapping("/{id}")
    @RequirePermission(groupCode = "donation", permission = "category.update")
    public DonationCategoryResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDonationCategoryRequest request) {

        return service.update(id, request);
    }

    @GetMapping("/{id}")
    @RequirePermission(groupCode = "donation", permission = "category.view")
    public DonationCategoryResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    @RequirePermission(groupCode = "donation", permission = "category.view")
    public List<DonationCategoryResponse> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequirePermission(groupCode = "donation", permission = "category.delete")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}