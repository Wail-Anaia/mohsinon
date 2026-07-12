package com.mohsinon.modules.authorization.cache;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class InMemoryPermissionCache
        implements PermissionCache {

    private final ConcurrentHashMap<UUID, Set<String>> cache =
            new ConcurrentHashMap<>();

    @Override
    public Set<String> get(UUID userId) {
        return cache.get(userId);
    }

    @Override
    public void put(
            UUID userId,
            Set<String> permissions) {

        cache.put(userId, permissions);
    }

    @Override
    public void evict(UUID userId) {
        cache.remove(userId);
    }

    @Override
    public void clear() {
        cache.clear();
    }

}