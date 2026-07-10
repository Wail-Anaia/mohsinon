package com.mohsinon.modules.test;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mohsinon.security.annotation.RequirePermission;
import com.mohsinon.security.annotation.ResourceId;

@RestController
public class AuthorizationTestController {

    @GetMapping("/api/test/{mosqueId}")
    @RequirePermission(
            groupCode = "mosque",
            permission = "mosque.view"
    )
    public String test(
            @ResourceId
            @PathVariable UUID mosqueId) {

        return "Authorization Success";
    }
}