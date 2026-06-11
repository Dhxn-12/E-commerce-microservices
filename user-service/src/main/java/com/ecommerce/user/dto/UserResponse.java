package com.ecommerce.user.dto;

import com.ecommerce.user.model.User;
import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private boolean active;
    private LocalDateTime createdAt;

    // Convert User entity to UserResponse (never expose password)
    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id        = user.getId();
        response.firstName = user.getFirstName();
        response.lastName  = user.getLastName();
        response.email     = user.getEmail();
        response.phone     = user.getPhone();
        response.role      = user.getRole().name();
        response.active    = user.isActive();
        response.createdAt = user.getCreatedAt();
        return response;
    }

    // Getters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
