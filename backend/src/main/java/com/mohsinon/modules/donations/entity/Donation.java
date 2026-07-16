package com.mohsinon.modules.donations.entity;

import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.shared.entity.LifecycleEntity;
import com.mohsinon.modules.donations.model.DonationStatus;
import com.mohsinon.modules.donations.model.DonationType;

import jakarta.persistence.*;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "donations")
public class Donation extends LifecycleEntity{

    @ManyToOne(optional = false)
    private Mosque mosque;

    @ManyToOne(optional = false)
    private DonationCategory category;

    @ManyToOne
    private User donor;

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status = DonationStatus.PENDING;

    private Double quantity;

    private BigDecimal estimatedValue;

    private String currency;

    /*
     * Business Methods
     */

    public void receive() {

        if (status != DonationStatus.PENDING) {
            throw new IllegalStateException("Donation cannot be received.");
        }

        status = DonationStatus.RECEIVED;
    }

    public void allocate() {

        if (status != DonationStatus.RECEIVED &&
                status != DonationStatus.IN_STORAGE) {
            throw new IllegalStateException("Donation cannot be allocated.");
        }

        status = DonationStatus.ALLOCATED;
    }

    public void deliver() {

        if (status != DonationStatus.ALLOCATED) {
            throw new IllegalStateException("Donation cannot be delivered.");
        }

        status = DonationStatus.DELIVERED;
    }

    public void cancel() {

        if (status == DonationStatus.DELIVERED) {
            throw new IllegalStateException("Delivered donation cannot be cancelled.");
        }

        status = DonationStatus.CANCELLED;
    }

}