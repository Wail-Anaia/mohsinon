package com.mohsinon.modules.donations.service;

import com.mohsinon.modules.donations.dto.request.CreateDonationCategoryRequest;
import com.mohsinon.modules.donations.dto.request.UpdateDonationCategoryRequest;
import com.mohsinon.modules.donations.dto.response.DonationCategoryResponse;
import com.mohsinon.modules.donations.entity.DonationCategory;
import com.mohsinon.modules.donations.exception.DonationCategoryNotFoundException;
import com.mohsinon.modules.donations.mapper.DonationCategoryMapper;
import com.mohsinon.modules.donations.repository.DonationCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DonationCategoryService {

    private final DonationCategoryRepository repository;
    private final DonationCategoryMapper mapper;

    public DonationCategoryService(
            DonationCategoryRepository repository,
            DonationCategoryMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;
    }

    public DonationCategoryResponse create(CreateDonationCategoryRequest request) {

        repository.findByCode(request.getCode())
                .ifPresent(category -> {
                    throw new IllegalArgumentException(
                            "Donation category code already exists.");
                });

        DonationCategory category = mapper.toEntity(request);

        category = repository.save(category);

        return mapper.toResponse(category);
    }

    public DonationCategoryResponse update(
            UUID id,
            UpdateDonationCategoryRequest request) {

        DonationCategory category = repository.findById(id)
                .orElseThrow(DonationCategoryNotFoundException::new);

        mapper.updateEntity(category, request);

        category = repository.save(category);

        return mapper.toResponse(category);
    }

    public DonationCategoryResponse findById(UUID id) {

        DonationCategory category = repository.findById(id)
                .orElseThrow(DonationCategoryNotFoundException::new);

        return mapper.toResponse(category);
    }

    public List<DonationCategoryResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void delete(UUID id) {

        DonationCategory category = repository.findById(id)
                .orElseThrow(DonationCategoryNotFoundException::new);

        repository.delete(category);
    }
}