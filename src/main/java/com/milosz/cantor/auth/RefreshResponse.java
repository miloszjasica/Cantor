package com.milosz.cantor.auth;

public class RefreshResponse {
    
    private String accessToken;
    private String tokenType;
    private long expiresInSeconds;

    public RefreshResponse() {
    }

    public RefreshResponse(String accessToken, String tokenType, long expiresInSeconds) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }
    
}
