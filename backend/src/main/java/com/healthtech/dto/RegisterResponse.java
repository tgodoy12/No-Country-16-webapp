package com.healthtech.dto;

import com.healthtech.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private boolean enabled;
    private LocalDateTime createdAt;

    public static RegisterResponse fromUser(User user) {
        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole().name());
        response.setEnabled(user.isEnabled());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}