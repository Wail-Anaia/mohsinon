package com.mohsinon.modules.mosques.controller;

import com.mohsinon.modules.mosques.dto.request.CreateMosqueRequest;
import com.mohsinon.modules.mosques.dto.response.MosqueResponse;
import com.mohsinon.modules.mosques.service.MosqueService;
import com.mohsinon.shared.query.response.PageResponse;
import com.mohsinon.shared.query.request.SearchRequest;
import com.mohsinon.common.api.ApiConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_V1 + "/mosques")
public class MosqueController {

    private final MosqueService mosqueService;

    public MosqueController(MosqueService mosqueService) {
        this.mosqueService = mosqueService;
    }

    @PostMapping
    public ResponseEntity<MosqueResponse> create(
            @Valid @RequestBody CreateMosqueRequest request) {

        MosqueResponse response = mosqueService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<MosqueResponse>> search(

            SearchRequest request,

            @RequestParam Map<String,String> filters) {

        return ResponseEntity.ok(
                mosqueService.search(
                        request,
                        filters));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MosqueResponse> findById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(mosqueService.findById(id));
    }
    
    @GetMapping
    public PageResponse<MosqueResponse> getAllMosques(

            @ModelAttribute SearchRequest request,

            @RequestParam Map<String, String> filters) {

        return mosqueService.search(
                request,
                filters);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MosqueResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateMosqueRequest request) {

        return ResponseEntity.ok(
                mosqueService.update(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {

        mosqueService.delete(id);

        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/restore")
    public ResponseEntity<MosqueResponse> restoreDeleted(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                mosqueService.restoreDeleted(id));
    }
    
    @PatchMapping("/{id}/archive")
    public ResponseEntity<MosqueResponse> archive(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                mosqueService.archive(id));
    }
    
    @PatchMapping("/{id}/restore-archive")
    public ResponseEntity<MosqueResponse> restoreArchive(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                mosqueService.restoreArchive(id));
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<MosqueResponse> activate(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                mosqueService.activate(id));
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<MosqueResponse> deactivate(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                mosqueService.deactivate(id));
    }

}