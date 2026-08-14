package com.mohsinon.core.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/test/errors")
public class TestErrorController {

    @GetMapping("/not-found")
    public void throwNotFound() {
        throw new ResourceNotFoundException("Mosque", UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

    @GetMapping("/conflict")
    public void throwConflict() {
        throw new ConflictException("USER_EMAIL_EXISTS", "A user with this email address already exists.");
    }

    @GetMapping("/forbidden")
    public void throwForbidden() {
        throw new ForbiddenException("CANNOT_EDIT_MOSQUE", "You are not authorized to edit this mosque profile.");
    }

    @GetMapping("/validation")
    public void throwValidation() {
        throw new ValidationException("Invalid payload", Map.of("email", "Must be a valid email address"));
    }
}
