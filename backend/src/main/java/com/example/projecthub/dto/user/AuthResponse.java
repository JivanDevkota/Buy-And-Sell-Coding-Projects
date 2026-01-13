package com.example.projecthub.dto.user;

import com.example.projecthub.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private Long userId;
    private String email;
    private Set<String>roles;
}
