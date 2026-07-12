package com.mohsinon.modules.mosques.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mohsinon.modules.mosques.dto.response.MosqueDashboardResponse;
import com.mohsinon.modules.mosques.service.MosqueStatisticsService;
import com.mohsinon.security.annotation.RequirePermission;
import com.mohsinon.security.annotation.ResourceId;

@RestController
public class MosqueDashboardController {

    private final MosqueStatisticsService statisticsService;

    public MosqueDashboardController(
            MosqueStatisticsService statisticsService) {

        this.statisticsService = statisticsService;
    }

    @GetMapping("/api/mosques/{mosqueId}/dashboard")
    @RequirePermission(
            groupCode = "mosque",
            permission = "dashboard.view")
    public MosqueDashboardResponse dashboard(
            @PathVariable
            @ResourceId
            UUID mosqueId) {

        return statisticsService.getDashboard(mosqueId);
    }
}