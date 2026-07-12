package com.mohsinon.modules.mosques.dashboard.provider;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MosqueDashboardRegistry {

    private final List<MosqueDashboardProvider> providers;

    public MosqueDashboardRegistry(
            List<MosqueDashboardProvider> providers) {

        this.providers = providers;
    }

    public List<MosqueDashboardProvider> getProviders() {
        return providers;
    }

}