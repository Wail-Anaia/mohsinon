package com.mohsinon.modules.mosques.service;

import com.mohsinon.modules.mosques.dto.request.CreateMosqueRequest;
import com.mohsinon.modules.mosques.dto.response.MosqueResponse;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.mapper.MosqueMapper;
import com.mohsinon.modules.mosques.repository.MosqueRepository;
import com.mohsinon.exception.MosqueNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MosqueService {

    private final MosqueRepository mosqueRepository;

    public MosqueService(MosqueRepository mosqueRepository) {
        this.mosqueRepository = mosqueRepository;
    }

    public MosqueResponse createMosque(CreateMosqueRequest request) {

        Mosque mosque = MosqueMapper.toEntity(request);

        Mosque savedMosque = mosqueRepository.save(mosque);

        return MosqueMapper.toResponse(savedMosque);
    }

    public List<MosqueResponse> getAllMosques() {

        return mosqueRepository.findAll()
                .stream()
                .map(MosqueMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MosqueResponse getMosqueById(UUID id) {

        Mosque mosque = mosqueRepository.findById(id)
        		.orElseThrow(MosqueNotFoundException::new);

        return MosqueMapper.toResponse(mosque);
    }

}