package com.example.projecthub.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleCountDTO {
    private String roleName;
    private Long count;
}
