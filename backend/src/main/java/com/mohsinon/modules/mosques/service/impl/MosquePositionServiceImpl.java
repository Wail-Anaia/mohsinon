package com.mohsinon.modules.mosques.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.mosques.exception.MosquePositionNotFoundException;
import com.mohsinon.modules.mosques.repository.MosquePositionRepository;
import com.mohsinon.modules.mosques.service.MosquePositionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MosquePositionServiceImpl
        implements MosquePositionService {

    private final MosquePositionRepository repository;

    @Override
    public MosquePosition getPositionById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new MosquePositionNotFoundException(
                                "Mosque position with id '" + id + "' was not found."));
    }

    @Override
    public MosquePosition getPositionByCode(String code) {

        return repository.findByCode(code)
                .orElseThrow(() ->
                        new MosquePositionNotFoundException(code));
    }

}