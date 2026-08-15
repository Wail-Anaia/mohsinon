package com.mohsinon.modules.identity.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    @DisplayName("Should normalize username and email to lowercase and trim whitespace")
    void shouldNormalizeUsernameAndEmail() {
        User user = new User("  User_One  ", "  USER@EXAMPLE.COM  ", "hash", "Fatima", "Zahra", null);

        assertThat(user.getUsername()).isEqualTo("user_one");
        assertThat(user.getEmail()).isEqualTo("user@example.com");
        assertThat(user.getFirstName()).isEqualTo("Fatima");
        assertThat(user.getLastName()).isEqualTo("Zahra");
        assertThat(user.getDisplayName()).isEqualTo("Fatima Zahra");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when username or email is blank")
    void shouldThrowOnBlankUsernameOrEmail() {
        assertThatThrownBy(() -> new User("", "test@example.com", "hash", null, null, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new User("username", "", "hash", null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should fall back to username when names are not provided")
    void shouldFallbackToUsernameForDisplayName() {
        User user = new User("bilal_k", "bilal@example.com", "hash", null, null, null);
        assertThat(user.getDisplayName()).isEqualTo("bilal_k");
    }
}
