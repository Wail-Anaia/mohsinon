package com.mohsinon.modules.mosques.dashboard.provider;

import java.util.UUID;

import com.mohsinon.modules.mosques.dto.response.MosqueDashboardResponse;

public interface MosqueDashboardProvider {

	String getCode();

    void enrich(
            UUID mosqueId,
            MosqueDashboardResponse dashboard);

}