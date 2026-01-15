package com.example.projecthub.service.admin;

import com.example.projecthub.model.Status;

import java.util.Map;

public interface AdminService {
    void updateUserStatus(
            Long userId,
            Status newStatus,
            String reason,
            String changedBy
    );

    Map<String, Object> getRecentlyJoinedUsersPaginated(int page, int size);
}
