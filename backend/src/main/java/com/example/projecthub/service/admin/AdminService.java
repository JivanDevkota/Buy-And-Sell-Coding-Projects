package com.example.projecthub.service.admin;

import com.example.projecthub.dto.category.CategoryDTO;
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

    Map<String, Object> getRecentAddedCategoriesPaginated(int page, int size);

    CategoryDTO createCategory(CategoryDTO dto);
}
