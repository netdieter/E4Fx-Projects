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
}
