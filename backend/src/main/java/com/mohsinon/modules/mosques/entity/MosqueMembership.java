package com.mohsinon.modules.mosques.entity;

import com.mohsinon.modules.mosques.membership.model.MembershipStatus;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.shared.entity.AuditableEntity;

import jakarta.persistence.*;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mosque_memberships")
public class MosqueMembership extends AuditableEntity{

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mosque_id", nullable = false)
    private Mosque mosque;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "position_id", nullable = false)
    private MosquePosition position;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MembershipStatus status = MembershipStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointed_by")
    private User appointedBy;

    @Column(length = 2000)
    private String notes;

    public MosqueMembership() {
    }

    @PrePersist
    public void prePersist() {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
    }
    
    public void invite() {
        this.status = MembershipStatus.INVITED;
    }

    public void activate() {
        this.status = MembershipStatus.ACTIVE;

        if (this.startDate == null) {
            this.startDate = LocalDate.now();
        }

        this.endDate = null;
    }

    public void suspend() {
        this.status = MembershipStatus.SUSPENDED;
    }

    public void reject() {
        this.status = MembershipStatus.REJECTED;

        if (this.endDate == null) {
            this.endDate = LocalDate.now();
        }
    }

    public void terminate() {
        this.status = MembershipStatus.TERMINATED;

        if (this.endDate == null) {
            this.endDate = LocalDate.now();
        }
    }
    
    public boolean isInvited() {
        return status == MembershipStatus.INVITED;
    }

    public boolean isPending() {
        return status == MembershipStatus.PENDING;
    }

    public boolean isActive() {
        return status == MembershipStatus.ACTIVE;
    }

    public boolean isSuspended() {
        return status == MembershipStatus.SUSPENDED;
    }

    public boolean isRejected() {
        return status == MembershipStatus.REJECTED;
    }

    public boolean isTerminated() {
        return status == MembershipStatus.TERMINATED;
    }
}