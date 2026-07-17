package com.mohsinon.modules.users.controller;

import com.mohsinon.common.api.ApiConstants;
import com.mohsinon.shared.documentation.SwaggerTags;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(ApiConstants.API_V1 + "/users")
@Tag(
	    name = SwaggerTags.USERS,
	    description = "إدارة المستخدمين"
	)
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

	@GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {

        return Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities()
        );
    }
}