package com.mohsinon.security.authorization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AuthorizationRegistry {

    private final Map<String, AuthorizationProvider> providers =
            new HashMap<>();

    public AuthorizationRegistry(
            List<AuthorizationProvider> authorizationProviders) {

        for (AuthorizationProvider provider : authorizationProviders) {

            providers.put(
                    provider.getGroupCode(),
                    provider
            );
        }
    }

    public AuthorizationProvider getProvider(String groupCode) {

        AuthorizationProvider provider =
                providers.get(groupCode);

        if (provider == null) {

            throw new IllegalArgumentException(
                    "No AuthorizationProvider registered for group: "
                            + groupCode
            );
        }

        return provider;
    }

}