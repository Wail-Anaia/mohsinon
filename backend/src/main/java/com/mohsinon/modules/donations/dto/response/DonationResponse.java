package com.mohsinon.modules.donations.dto.response;

import com.mohsinon.modules.donations.model.DonationStatus;
import com.mohsinon.modules.donations.model.DonationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DonationResponse {

    private UUID id;

    private UUID mosqueId;

    private UUID categoryId;

    private UUID donorId;

    private String title;

    private String description;

    private DonationType type;

    private DonationStatus status;

    private Double quantity;

    private BigDecimal estimatedValue;

    private String currency;

    private LocalDateTime createdAt;

}