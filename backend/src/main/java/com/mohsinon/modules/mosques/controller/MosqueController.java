package com.mohsinon.modules.mosques.controller;

import com.mohsinon.modules.mosques.dto.request.CreateMosqueRequest;
import com.mohsinon.modules.mosques.dto.response.MosqueResponse;
import com.mohsinon.modules.mosques.service.MosqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mosques")
public class MosqueController {

    private final MosqueService mosqueService;

    public MosqueController(MosqueService mosqueService) {
        this.mosqueService = mosqueService;
    }

    @PostMapping
    public ResponseEntity<MosqueResponse> createMosque(
            @Valid @RequestBody CreateMosqueRequest request) {

        MosqueResponse response = mosqueService.createMosque(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MosqueResponse>> getAllMosques() {

        return ResponseEntity.ok(mosqueService.getAllMosques());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MosqueResponse> getMosqueById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(mosqueService.getMosqueById(id));
    }

}