package com.mohsinon.modules.donations.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDonationCategoryRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Boolean active;

    @NotNull
    private Integer sortOrder;

}