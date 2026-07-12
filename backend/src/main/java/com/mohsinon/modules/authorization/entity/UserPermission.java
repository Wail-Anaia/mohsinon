package com.mohsinon.modules.authorization.entity;

import java.time.LocalDateTime;

import com.mohsinon.modules.users.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "user_permissions",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "user_id",
                "permission_id"
            }
        )
    }
)
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
    
    @Column(nullable = false)
    private Boolean granted = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

}