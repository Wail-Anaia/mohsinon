package com.mohsinon.core.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Core Health", description = "Endpoints for verifying system health and core metadata")
public class CoreHealthController {

    @GetMapping
    @Operation(summary = "Check Mohsinon API health status")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "mohsinon-backend",
                "version", "0.1.0-alpha",
                "timestamp", Instant.now().toString(),
                "mode", "MODULAR_MONOLITH"
        ));
    }
}
