package com.mohsinon.modules.identity.domain;

import com.mohsinon.core.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Locale;
import java.util.UUID;

/**
 * Root Identity entity representing a user in the Mohsinon platform.
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_username", columnNames = "username")
        },
        indexes = {
                @Index(name = "idx_users_email", columnList = "email"),
                @Index(name = "idx_users_username", columnList = "username"),
                @Index(name = "idx_users_status", columnList = "status")
        }
)
public class User extends BaseEntity {

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "display_name", length = 150)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private UserStatus status = UserStatus.ACTIVE;

    public User() {
    }

    public User(String username, String email, String passwordHash, String firstName, String lastName, String displayName) {
        setUsername(username);
        setEmail(email);
        this.passwordHash = passwordHash;
        this.firstName = firstName != null ? firstName.trim() : null;
        this.lastName = lastName != null ? lastName.trim() : null;
        this.displayName = displayName != null && !displayName.trim().isEmpty()
                ? displayName.trim()
                : computeDefaultDisplayName(this.firstName, this.lastName, this.username);
        this.status = UserStatus.ACTIVE;
    }

    public static User createActive(String username, String email, String passwordHash, String firstName, String lastName) {
        return new User(username, email, passwordHash, firstName, lastName, null);
    }

    private static String computeDefaultDisplayName(String firstName, String lastName, String username) {
        if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) {
            return firstName + " " + lastName;
        } else if (firstName != null && !firstName.isEmpty()) {
            return firstName;
        }
        return username;
    }

    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return displayName != null ? displayName : username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty.");
        }
        this.username = username.trim().toLowerCase(Locale.ROOT);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty.");
        }
        this.email = email.trim().toLowerCase(Locale.ROOT);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash must not be empty.");
        }
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName != null ? firstName.trim() : null;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName != null ? lastName.trim() : null;
    }

    public String getDisplayName() {
        return displayName != null ? displayName : getFullName();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName != null ? displayName.trim() : null;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status != null ? status : UserStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }
}
