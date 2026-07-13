package com.mohsinon.modules.donations.dto.request;

import com.mohsinon.modules.donations.model.DonationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDonationRequest {

    @NotNull
    private UUID mosqueId;

    @NotNull
    private UUID categoryId;

    private UUID donorId;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private DonationType type;

    private Double quantity;

    private BigDecimal estimatedValue;

    private String currency;

}