package com.example.projecthub.dto.user;

import com.example.projecthub.model.Role;
import com.example.projecthub.model.Status;
import com.example.projecthub.model.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDetailsResponse {
    private Long id;
    private String username;
    private String email;
    private Set<String> role;
    private LocalDateTime joined;
    private Status status;


    public static UserDetailsResponse toDto(User user) {
        UserDetailsResponse dto = new UserDetailsResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setJoined(user.getCreatedAt());
        dto.setRole(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return dto;
    }
}

