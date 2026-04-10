package com.shoppinglist.backend.security;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ApiKeyAuthFilter implements ContainerRequestFilter {

    @ConfigProperty(name = "shoppinglist.api.key", defaultValue = "")
    String configuredApiKey;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Pfad für öffentliche Endpunkte (z.B. Health-Checks)
        String path = requestContext.getUriInfo().getPath();
        if (path.startsWith("q/health") || path.startsWith("api/public")) {
            return;
        }

        String apiKey = requestContext.getHeaderString("X-API-Key");

        if (apiKey == null || apiKey.isEmpty()) {
            requestContext.abortWith(
                jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"API-Key fehlt\"}")
                    .type(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                    .build()
            );
            return;
        }

        // Wenn kein API-Key konfiguriert ist, erlauben wir alle Requests (für Entwicklung)
        if (configuredApiKey == null || configuredApiKey.isEmpty()) {
            requestContext.setProperty("api-key", apiKey);
            return;
        }

        if (!configuredApiKey.equals(apiKey)) {
            requestContext.abortWith(
                jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.FORBIDDEN)
                    .entity("{\"error\": \"Ungültiger API-Key\"}")
                    .type(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                    .build()
            );
        } else {
            requestContext.setProperty("api-key", apiKey);
        }
    }
}
