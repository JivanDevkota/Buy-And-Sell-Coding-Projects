package com.example.projecthub.service.admin;

import com.example.projecthub.dto.category.CategoryDTO;
import com.example.projecthub.model.Category;
import com.example.projecthub.model.Status;

import java.util.List;
import java.util.Map;

public interface AdminService {
    void updateUserStatus(
            Long userId,
            Status newStatus,
            String reason,
            String changedBy
    );

    Map<String, Object> getRecentlyJoinedUsersPaginated(int page, int size);
    Map<String, Object> getAllJoinedUsersPaginated(int page, int size);

    Map<String, Object> getRecentAddedCategoriesPaginated(int page, int size);

    List<CategoryDTO> getAllCategory();
    CategoryDTO createCategory(CategoryDTO dto);
}
