package com.milosz.cantor.auth;

class AuthResponse {

    private String userId;
    private String email;

    public AuthResponse() {
    }

    public AuthResponse(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
