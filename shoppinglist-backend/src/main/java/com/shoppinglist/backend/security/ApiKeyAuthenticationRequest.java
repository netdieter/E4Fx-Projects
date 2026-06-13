package com.shoppinglist.backend.security;

import io.quarkus.security.identity.request.AuthenticationRequest;

public class ApiKeyAuthenticationRequest implements AuthenticationRequest {

    private final String apiKey;

    public ApiKeyAuthenticationRequest(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public java.util.Map<String, Object> getAttributes() {
        return java.util.Collections.emptyMap();
    }

    @Override
    public void setAttribute(String name, Object value) {
        // do nothing
    }

    @Override
    public <T> T getAttribute(String name) {
        return null;
    }
}
