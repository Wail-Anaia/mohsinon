package com.mohsinon.modules.mosques.api;

import com.mohsinon.modules.mosques.entity.MosquePosition;

public interface MosqueApi {

    MosquePosition getPositionById(Long positionId);
    
    MosquePosition getPositionByCode(String code);

}