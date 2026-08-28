package com.mohsinon.core.security.authorization.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceContextTest {

    @Test
    @DisplayName("Should create ResourceContext via factory methods")
    void shouldCreateViaFactoryMethods() {
        UUID mosqueId = UUID.randomUUID();
        ResourceContext context = ResourceContext.mosque(mosqueId);

        assertThat(context.resourceType()).isEqualTo("MOSQUE");
        assertThat(context.resourceId()).isEqualTo(mosqueId);
    }

    @Test
    @DisplayName("Should normalize resource type to uppercase and trim")
    void shouldNormalizeResourceType() {
        UUID id = UUID.randomUUID();
        ResourceContext context = ResourceContext.of("  project  ", id);

        assertThat(context.resourceType()).isEqualTo("PROJECT");
        assertThat(context.resourceId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Should reject null or empty parameters")
    void shouldRejectInvalidParams() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> ResourceContext.of(null, id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Resource type must not be null or empty");

        assertThatThrownBy(() -> ResourceContext.of("   ", id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Resource type must not be null or empty");

        assertThatThrownBy(() -> ResourceContext.of("MOSQUE", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Resource ID must not be null");
    }

    @Test
    @DisplayName("Equality and hashCode should work correctly for record")
    void equalityAndHashCode() {
        UUID id = UUID.randomUUID();
        ResourceContext ctx1 = ResourceContext.mosque(id);
        ResourceContext ctx2 = ResourceContext.of("MOSQUE", id);

        assertThat(ctx1).isEqualTo(ctx2);
        assertThat(ctx1.hashCode()).isEqualTo(ctx2.hashCode());
    }
}
