package com.example.projecthub.dto.dashboard;

import lombok.Data;

import java.util.Map;

@Data
public class DashboardAdminStats {
    Map<String, Long> roleCounts;
    private Long totalUsers;
    private Long activeUsers;
    private Long pendingUsers;
}
