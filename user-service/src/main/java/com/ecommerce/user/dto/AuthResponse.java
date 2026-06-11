package com.ecommerce.user.dto;

public class AuthResponse {

    private String token;
    private String email;
    private String role;
    private Long userId;
    private String message;

    public AuthResponse(String token, String email, String role, Long userId, String message) {
        this.token   = token;
        this.email   = email;
        this.role    = role;
        this.userId  = userId;
        this.message = message;
    }

    public String getToken()   { return token; }
    public String getEmail()   { return email; }
    public String getRole()    { return role; }
    public Long getUserId()    { return userId; }
    public String getMessage() { return message; }
}
