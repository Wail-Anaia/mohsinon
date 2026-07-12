package com.mohsinon.modules.mosques.api;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.mosques.service.MosquePositionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MosqueApiImpl implements MosqueApi {

    private final MosquePositionService positionService;

    @Override
    public MosquePosition getPositionById(Long positionId) {
        return positionService.getPositionById(positionId);
    }

    @Override
    public MosquePosition getPositionByCode(String code) {
        return positionService.getPositionByCode(code);
    }

}