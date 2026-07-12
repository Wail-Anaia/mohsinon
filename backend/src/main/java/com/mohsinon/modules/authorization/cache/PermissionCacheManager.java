package com.mohsinon.modules.authorization.cache;

import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermissionCacheManager {

    private final PermissionCache cache;

    public Set<String> get(UUID userId) {
        return cache.get(userId);
    }

    public void put(
            UUID userId,
            Set<String> permissions) {

        cache.put(userId, permissions);
    }

    public void evict(UUID userId) {
        cache.evict(userId);
    }

    public void clear() {
        cache.clear();
    }

}