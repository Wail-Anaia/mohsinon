package com.mohsinon.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BaseEntityTest {

    static class TestEntity extends BaseEntity {
        public TestEntity() {
            super();
        }

        public TestEntity(UUID id) {
            super(id);
        }
    }

    @Test
    @DisplayName("Entities with same UUID should be equal")
    void entitiesWithSameIdShouldBeEqual() {
        UUID id = UUID.randomUUID();
        TestEntity entity1 = new TestEntity(id);
        TestEntity entity2 = new TestEntity(id);

        assertThat(entity1).isEqualTo(entity2);
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    @DisplayName("Entities with different UUIDs should not be equal")
    void entitiesWithDifferentIdsShouldNotBeEqual() {
        TestEntity entity1 = new TestEntity(UUID.randomUUID());
        TestEntity entity2 = new TestEntity(UUID.randomUUID());

        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    @DisplayName("New entity without ID should be identified as new")
    void newEntityShouldBeIdentified() {
        TestEntity entity = new TestEntity();
        assertThat(entity.isNew()).isTrue();
        assertThat(entity.getId()).isNull();

        UUID id = UUID.randomUUID();
        entity.setId(id);
        assertThat(entity.isNew()).isFalse();
        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("BaseEntity should initialize timestamps and version default")
    void shouldInitializeTimestampsAndVersion() {
        TestEntity entity = new TestEntity();
        assertThat(entity.getCreatedAt()).isNotNull().isBeforeOrEqualTo(Instant.now());
        assertThat(entity.getUpdatedAt()).isNotNull().isBeforeOrEqualTo(Instant.now());
        assertThat(entity.getVersion()).isEqualTo(0L);
    }
}
