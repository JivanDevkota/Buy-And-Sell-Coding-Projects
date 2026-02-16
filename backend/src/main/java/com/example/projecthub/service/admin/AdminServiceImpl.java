package com.example.projecthub.service.admin;

import com.example.projecthub.dto.category.CategoryDTO;
import com.example.projecthub.dto.user.UserDetailsResponse;
import com.example.projecthub.model.Category;
import com.example.projecthub.model.Status;
import com.example.projecthub.model.User;
import com.example.projecthub.model.UserStatusHistory;
import com.example.projecthub.repository.CategoryRepository;
import com.example.projecthub.repository.UserRepository;
import com.example.projecthub.repository.UserStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserStatusHistoryRepository userStatusHistoryRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void updateUserStatus(
            Long userId,
            Status newStatus,
            String reason,
            String changedBy
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Status currentStatus = user.getStatus();

        // 🔒 VALIDATE TRANSITION (clean & professional)
        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    "Invalid status transition: " + currentStatus + " → " + newStatus
            );
        }

        // 📝 SAVE HISTORY
        UserStatusHistory history = new UserStatusHistory();
        history.setUser(user);
        history.setOldStatus(currentStatus);
        history.setNewStatus(newStatus);
        history.setReason(reason);
        history.setChangedBy(changedBy);
        history.setChangedAt(LocalDateTime.now());

        userStatusHistoryRepository.save(history);

        // ✅ UPDATE USER STATUS (YOU MISSED THIS EARLIER)
        user.setStatus(newStatus);
        userRepository.save(user);
    }

    public Map<String, Object> getRecentlyJoinedUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = userRepository.findAllByOrderByCreatedAtDesc(pageable);

        // FIX: Changed from Positive.List to List and fixed toDto() call
        List<UserDetailsResponse> users = userPage.getContent().stream()
                .map(UserDetailsResponse::toDto)  // Changed from toDto() to ::toDto
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("currentPage", userPage.getNumber());
        response.put("totalPages", userPage.getTotalPages());
        response.put("totalItems", userPage.getTotalElements());
        response.put("hasNext", userPage.hasNext());
        response.put("hasPrevious", userPage.hasPrevious());
        return response;
    }


    public Map<String, Object> getAllJoinedUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> userPage = userRepository.findAllByOrderByIdAsc(pageable);

        // FIX: Changed from Positive.List to List and fixed toDto() call
        List<UserDetailsResponse> users = userPage.getContent().stream()
                .map(UserDetailsResponse::toDto)  // Changed from toDto() to ::toDto
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("currentPage", userPage.getNumber());
        response.put("totalPages", userPage.getTotalPages());
        response.put("totalItems", userPage.getTotalElements());
        response.put("hasNext", userPage.hasNext());
        response.put("hasPrevious", userPage.hasPrevious());
        return response;
    }



    //category
    public CategoryDTO createCategory(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        Category savedCategory = categoryRepository.save(category);
        return CategoryDTO.toDto(savedCategory);
    }

    public Map<String, Object> getRecentAddedCategoriesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Category> categoryPage = categoryRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<CategoryDTO> categories = categoryPage.getContent().stream()
                .map(CategoryDTO::toDto).collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("categories", categories);
        response.put("currentPage", categoryPage.getNumber());
        response.put("totalPages", categoryPage.getTotalPages());
        response.put("totalItems", categoryPage.getTotalElements());
        response.put("hasNext", categoryPage.hasNext());
        response.put("hasPrevious", categoryPage.hasPrevious());
        return response;
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::toDto)
                .collect(Collectors.toList());
    }

}
