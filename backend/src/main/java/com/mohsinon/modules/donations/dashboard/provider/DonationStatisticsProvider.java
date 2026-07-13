package com.mohsinon.modules.donations.dashboard.provider;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mohsinon.modules.donations.model.DonationStatus;
import com.mohsinon.modules.donations.repository.DonationRepository;
import com.mohsinon.modules.mosques.dashboard.provider.MosqueDashboardProvider;
import com.mohsinon.modules.mosques.dto.response.MosqueDashboardResponse;
import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.exception.MosqueNotFoundException;
import com.mohsinon.modules.mosques.repository.MosqueRepository;

@Component
public class DonationStatisticsProvider
        implements MosqueDashboardProvider {

    private final MosqueRepository mosqueRepository;
    private final DonationRepository donationRepository;

    public DonationStatisticsProvider(
            MosqueRepository mosqueRepository,
            DonationRepository donationRepository) {

        this.mosqueRepository = mosqueRepository;
        this.donationRepository = donationRepository;
    }

    @Override
    public String getCode() {
        return "donation";
    }

    @Override
    public void enrich(
            UUID mosqueId,
            MosqueDashboardResponse dashboard) {

        Mosque mosque = mosqueRepository.findById(mosqueId)
                .orElseThrow(MosqueNotFoundException::new);

        dashboard.setTotalDonations(
                donationRepository.findByMosque(mosque).size());

        dashboard.setPendingDonations(
                donationRepository.findByMosque(mosque)
                        .stream()
                        .filter(d -> d.getStatus() == DonationStatus.PENDING)
                        .count());

        dashboard.setReceivedDonations(
                donationRepository.findByMosque(mosque)
                        .stream()
                        .filter(d -> d.getStatus() == DonationStatus.RECEIVED)
                        .count());

        dashboard.setAllocatedDonations(
                donationRepository.findByMosque(mosque)
                        .stream()
                        .filter(d -> d.getStatus() == DonationStatus.ALLOCATED)
                        .count());

        dashboard.setDeliveredDonations(
                donationRepository.findByMosque(mosque)
                        .stream()
                        .filter(d -> d.getStatus() == DonationStatus.DELIVERED)
                        .count());

        dashboard.setCancelledDonations(
                donationRepository.findByMosque(mosque)
                        .stream()
                        .filter(d -> d.getStatus() == DonationStatus.CANCELLED)
                        .count());
    }
}