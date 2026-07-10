package com.mohsinon.modules.mosques.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public class MosqueMembershipResponse {

    private UUID id;

    private UUID mosqueId;
    private String mosqueName;

    private UUID userId;
    private String fullName;

    private String positionCode;
    private String positionName;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean active;

    private UUID appointedById;
    private String appointedByName;

    private String notes;

    public MosqueMembershipResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMosqueId() {
        return mosqueId;
    }

    public void setMosqueId(UUID mosqueId) {
        this.mosqueId = mosqueId;
    }

    public String getMosqueName() {
        return mosqueName;
    }

    public void setMosqueName(String mosqueName) {
        this.mosqueName = mosqueName;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UUID getAppointedById() {
        return appointedById;
    }

    public void setAppointedById(UUID appointedById) {
        this.appointedById = appointedById;
    }

    public String getAppointedByName() {
        return appointedByName;
    }

    public void setAppointedByName(String appointedByName) {
        this.appointedByName = appointedByName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}