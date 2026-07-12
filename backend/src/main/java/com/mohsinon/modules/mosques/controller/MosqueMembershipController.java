package com.mohsinon.modules.mosques.controller;

import com.mohsinon.modules.mosques.dto.request.AssignMosquePositionRequest;
import com.mohsinon.modules.mosques.dto.request.ChangeImamRequest;
import com.mohsinon.modules.mosques.dto.response.MosqueMembershipResponse;
import com.mohsinon.modules.mosques.service.MosqueMembershipService;
import com.mohsinon.modules.users.repository.UserRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mosques")
public class MosqueMembershipController {

    private final MosqueMembershipService membershipService;

    public MosqueMembershipController(
            MosqueMembershipService membershipService,
            UserRepository userRepository) {

        this.membershipService = membershipService;
    }

    @PostMapping("/{mosqueId}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public MosqueMembershipResponse assignMember(
            @PathVariable UUID mosqueId,
            @Valid @RequestBody AssignMosquePositionRequest request) {


        return membershipService.assignPosition(
                mosqueId,
                request.getUserId(),
                request.getPositionCode(),
                request.getNotes()
        );
    }
    
    @GetMapping("/{mosqueId}/members")
    public List<MosqueMembershipResponse> getMembers(
            @PathVariable UUID mosqueId) {

        return membershipService.getActiveMembers(mosqueId);
    }
    
    @PostMapping("/{mosqueId}/imam")
    public MosqueMembershipResponse changeImam(
            @PathVariable UUID mosqueId,
            @Valid @RequestBody ChangeImamRequest request) {

        return membershipService.changeImam(
                mosqueId,
                request.getUserId(),
                request.getNotes()
        );
    }
    
    @PatchMapping("/members/{membershipId}/terminate")
    public MosqueMembershipResponse terminateMembership(
            @PathVariable UUID membershipId) {

        return membershipService.terminateMembership(
                membershipId
        );
    }
    
    @GetMapping("/{mosqueId}/imam")
    public MosqueMembershipResponse getCurrentImam(
            @PathVariable UUID mosqueId) {

        return membershipService.getCurrentImam(mosqueId);
    }
    
    @GetMapping("/{mosqueId}/imams/history")
    public List<MosqueMembershipResponse> getImamHistory(
            @PathVariable UUID mosqueId) {

        return membershipService.getImamHistory(mosqueId);
    }
    
    @GetMapping("/users/{userId}/history")
    public List<MosqueMembershipResponse> getUserHistory(
            @PathVariable UUID userId) {

        return membershipService.getUserMembershipHistory(userId);
    }
    
}