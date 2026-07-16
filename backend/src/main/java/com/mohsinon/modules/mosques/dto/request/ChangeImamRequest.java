package com.mohsinon.modules.mosques.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeImamRequest {

    @NotNull
    private UUID userId;

    private String notes;

    public ChangeImamRequest() {
    }
}