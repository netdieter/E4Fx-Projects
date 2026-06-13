package com.shoppinglist.backend.security;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ApiKeyIdentityProvider implements IdentityProvider<ApiKeyAuthenticationRequest> {

    @ConfigProperty(name = "shoppinglist.api.key", defaultValue = "")
    String configuredApiKey;

    @Override
    public Class<ApiKeyAuthenticationRequest> getRequestType() {
        return ApiKeyAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(ApiKeyAuthenticationRequest request, AuthenticationRequestContext context) {
        String providedApiKey = request.getApiKey();

        if (providedApiKey == null || providedApiKey.isEmpty()) {
            return Uni.createFrom().failure(new SecurityException("API-Key fehlt"));
        }

        // Wenn kein API-Key konfiguriert ist, erlauben wir alle Requests (für Entwicklung)
        if (configuredApiKey == null || configuredApiKey.isEmpty()) {
            return Uni.createFrom().item(createSecurityIdentity(providedApiKey));
        }

        if (!configuredApiKey.equals(providedApiKey)) {
            return Uni.createFrom().failure(new SecurityException("Ungültiger API-Key"));
        }

        return Uni.createFrom().item(createSecurityIdentity(providedApiKey));
    }

    private SecurityIdentity createSecurityIdentity(String apiKey) {
        return new io.quarkus.security.runtime.QuarkusSecurityIdentity.Builder()
                .setPrincipal(new io.quarkus.security.runtime.QuarkusPrincipal("api-user"))
                .addAttribute("api-key", apiKey)
                .build();
    }
}
