package com.mohsinon.modules.mosques.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignMosquePositionRequest {

    @NotNull
    private UUID userId;

    @NotBlank
    private String positionCode;

    private String notes;

    public AssignMosquePositionRequest() {
    }

}