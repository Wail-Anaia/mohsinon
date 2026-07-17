package com.mohsinon.modules.donations.controller;

import com.mohsinon.modules.donations.dto.request.CreateDonationRequest;
import com.mohsinon.modules.donations.dto.response.DonationResponse;
import com.mohsinon.modules.donations.service.DonationService;
import com.mohsinon.common.api.ApiConstants;
import com.mohsinon.security.annotation.RequirePermission;
import com.mohsinon.shared.documentation.SwaggerTags;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_V1 + "/donations")
@Tag(
	    name = SwaggerTags.DONATIONS,
	    description = "إدارة التبرعات"
	)
@SecurityRequirement(name = "Bearer Authentication")
public class DonationController {

    private final DonationService service;

    public DonationController(DonationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequirePermission(groupCode = "donation", permission = "create")
    public DonationResponse create(
            @Valid @RequestBody CreateDonationRequest request) {

        return service.create(request);
    }

    @GetMapping("/{id}")
    @RequirePermission(groupCode = "donation", permission = "view")
    public DonationResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    @RequirePermission(groupCode = "donation", permission = "view")
    public List<DonationResponse> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequirePermission(groupCode = "donation", permission = "delete")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PatchMapping("/{id}/receive")
    @RequirePermission(groupCode = "donation", permission = "receive")
    public DonationResponse receive(@PathVariable UUID id) {
        return service.receive(id);
    }

    @PatchMapping("/{id}/allocate")
    @RequirePermission(groupCode = "donation", permission = "allocate")
    public DonationResponse allocate(@PathVariable UUID id) {
        return service.allocate(id);
    }

    @PatchMapping("/{id}/deliver")
    @RequirePermission(groupCode = "donation", permission = "deliver")
    public DonationResponse deliver(@PathVariable UUID id) {
        return service.deliver(id);
    }

    @PatchMapping("/{id}/cancel")
    @RequirePermission(groupCode = "donation", permission = "cancel")
    public DonationResponse cancel(@PathVariable UUID id) {
        return service.cancel(id);
    }
}