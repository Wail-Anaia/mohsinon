package com.mohsinon.modules.donations.service;

import com.mohsinon.modules.donations.dto.request.CreateDonationRequest;
import com.mohsinon.modules.donations.dto.response.DonationResponse;
import com.mohsinon.modules.donations.entity.Donation;
import com.mohsinon.modules.donations.entity.DonationCategory;
import com.mohsinon.modules.donations.exception.DonationCategoryNotFoundException;
import com.mohsinon.modules.donations.exception.DonationNotFoundException;
import com.mohsinon.modules.donations.mapper.DonationMapper;
import com.mohsinon.modules.donations.repository.DonationCategoryRepository;
import com.mohsinon.modules.donations.repository.DonationRepository;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.repository.MosqueRepository;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.modules.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final DonationCategoryRepository categoryRepository;
    private final MosqueRepository mosqueRepository;
    private final UserRepository userRepository;
    private final DonationMapper mapper;

    public DonationService(
            DonationRepository donationRepository,
            DonationCategoryRepository categoryRepository,
            MosqueRepository mosqueRepository,
            UserRepository userRepository,
            DonationMapper mapper) {

        this.donationRepository = donationRepository;
        this.categoryRepository = categoryRepository;
        this.mosqueRepository = mosqueRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public DonationResponse create(CreateDonationRequest request) {

        Mosque mosque = mosqueRepository.findById(request.getMosqueId())
                .orElseThrow(() ->
                        new RuntimeException("Mosque not found."));

        DonationCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new DonationCategoryNotFoundException(request.getCategoryId()));

        if (!category.isActive()) {
            throw new IllegalStateException(
                    "Donation category is inactive.");
        }

        User donor = null;

        if (request.getDonorId() != null) {

            donor = userRepository.findById(request.getDonorId())
                    .orElseThrow(() ->
                            new RuntimeException("Donor not found."));
        }

        if (request.getEstimatedValue() != null &&
                request.getEstimatedValue().compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Estimated value cannot be negative.");
        }

        if (request.getQuantity() != null &&
                request.getQuantity() < 0) {

            throw new IllegalArgumentException(
                    "Quantity cannot be negative.");
        }

        Donation donation =
                mapper.toEntity(request, mosque, category, donor);

        donation = donationRepository.save(donation);

        return mapper.toResponse(donation);
    }

    public DonationResponse findById(UUID id) {

        Donation donation = donationRepository.findById(id)
                .orElseThrow(() ->
                        new DonationNotFoundException(id));

        return mapper.toResponse(donation);
    }

    public List<DonationResponse> findAll() {

        return donationRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void delete(UUID id) {

        Donation donation = donationRepository.findById(id)
                .orElseThrow(() ->
                        new DonationNotFoundException(id));

        donationRepository.delete(donation);
    }

    public DonationResponse receive(UUID id) {

        Donation donation = donationRepository.findById(id)
                .orElseThrow(() ->
                        new DonationNotFoundException(id));

        donation.receive();

        donation = donationRepository.save(donation);

        return mapper.toResponse(donation);
    }

    public DonationResponse allocate(UUID id) {

        Donation donation = donationRepository.findById(id)
                .orElseThrow(() ->
                        new DonationNotFoundException(id));

        donation.allocate();

        donation = donationRepository.save(donation);

        return mapper.toResponse(donation);
    }

    public DonationResponse deliver(UUID id) {

        Donation donation = donationRepository.findById(id)
                .orElseThrow(() ->
                        new DonationNotFoundException(id));

        donation.deliver();

        donation = donationRepository.save(donation);

        return mapper.toResponse(donation);
    }

    public DonationResponse cancel(UUID id) {

        Donation donation = donationRepository.findById(id)
                .orElseThrow(() ->
                        new DonationNotFoundException(id));

        donation.cancel();

        donation = donationRepository.save(donation);

        return mapper.toResponse(donation);
    }
}