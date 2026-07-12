package com.mohsinon.modules.mosques.dashboard.service;

import com.mohsinon.modules.mosques.dashboard.provider.MosqueDashboardRegistry;
import com.mohsinon.modules.mosques.dto.response.MosqueDashboardResponse;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.exception.MosqueNotFoundException;
import com.mohsinon.modules.mosques.repository.MosqueRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MosqueDashboardService {

    private final MosqueRepository mosqueRepository;
    private final MosqueDashboardRegistry registry;

    public MosqueDashboardService(
            MosqueRepository mosqueRepository,
            MosqueDashboardRegistry registry) {

        this.mosqueRepository = mosqueRepository;
        this.registry = registry;
    }

    public MosqueDashboardResponse getDashboard(UUID mosqueId) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        MosqueDashboardResponse response =
                new MosqueDashboardResponse();

        response.setMosqueId(mosque.getId());
        response.setMosqueName(mosque.getName());

        registry.getProviders()
        .forEach(provider ->

                provider.enrich(
                        mosqueId,
                        response));

        return response;
    }
}