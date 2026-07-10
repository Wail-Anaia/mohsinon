package com.mohsinon.modules.mosques.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ChangeImamRequest {

    @NotNull
    private UUID userId;

    private String notes;

    public ChangeImamRequest() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}