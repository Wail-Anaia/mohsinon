package com.mohsinon.modules.identity.web;

import com.mohsinon.core.security.CurrentUserProvider;
import com.mohsinon.modules.identity.service.UserProfileService;
import com.mohsinon.modules.identity.web.dto.PrivateUserProfileResponse;
import com.mohsinon.modules.identity.web.dto.PublicUserProfileResponse;
import com.mohsinon.modules.identity.web.dto.UpdateUserProfileRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "User Profiles", description = "Endpoints for managing private user profiles and querying public community profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final CurrentUserProvider currentUserProvider;

    public UserProfileController(UserProfileService userProfileService, CurrentUserProvider currentUserProvider) {
        this.userProfileService = userProfileService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/api/v1/users/me")
    @Operation(
            summary = "Get current user private profile",
            description = "Retrieves the complete private profile for the authenticated account owner.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    public ResponseEntity<PrivateUserProfileResponse> getMyProfile() {
        UUID currentUserId = currentUserProvider.requireCurrentUserId();
        PrivateUserProfileResponse response = userProfileService.getMyProfile(currentUserId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/v1/users/me")
    @Operation(
            summary = "Update current user private profile",
            description = "Updates personal details, contact info, bio, and preferences for the authenticated user.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    public ResponseEntity<PrivateUserProfileResponse> updateMyProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        UUID currentUserId = currentUserProvider.requireCurrentUserId();
        PrivateUserProfileResponse response = userProfileService.updateMyProfile(currentUserId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/public/users/{username}")
    @Operation(
            summary = "Get public user profile by username",
            description = "Retrieves the public profile for an active user, strictly concealing private contact details and exact residence coordinates."
    )
    public ResponseEntity<PublicUserProfileResponse> getPublicProfile(@PathVariable("username") String username) {
        PublicUserProfileResponse response = userProfileService.getPublicProfileByUsername(username);
        return ResponseEntity.ok(response);
    }
}
