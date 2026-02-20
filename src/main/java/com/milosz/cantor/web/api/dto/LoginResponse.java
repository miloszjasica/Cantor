package com.milosz.cantor.web.api.dto;

public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresInSeconds;

    public LoginResponse() {
    }

    public LoginResponse(String accessToken, String refreshToken, String tokenType, long expiresInSeconds) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }
}
