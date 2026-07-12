package com.mohsinon.modules.mosques.service;

import com.mohsinon.modules.mosques.entity.MosquePosition;

public interface MosquePositionService {

    MosquePosition getPositionById(Long id);

    MosquePosition getPositionByCode(String code);

}