package com.example.projecthub.service.admin;

import com.example.projecthub.dto.category.CategoryDTO;
import com.example.projecthub.dto.dashboard.DashboardAdminStats;
import com.example.projecthub.dto.dashboard.RoleCountDTO;
import com.example.projecthub.dto.seller.SellerSummaryDTO;
import com.example.projecthub.dto.user.UserDetailsResponse;
import com.example.projecthub.model.Category;
import com.example.projecthub.model.Status;
import com.example.projecthub.model.User;
import com.example.projecthub.model.UserStatusHistory;
import com.example.projecthub.repository.CategoryRepository;
import com.example.projecthub.repository.SellerRepository;
import com.example.projecthub.repository.UserRepository;
import com.example.projecthub.repository.UserStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
    private final SellerRepository sellerRepository;

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

    /**
     * Helper method to paginate users and build response map
     */
    private Map<String, Object> buildUserPaginationResponse(Page<User> userPage) {
        List<UserDetailsResponse> users = userPage.getContent().stream()
                .map(UserDetailsResponse::toDto)
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

    public Map<String, Object> getRecentlyJoinedUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = userRepository.findAllByOrderByCreatedAtDesc(pageable);
        return buildUserPaginationResponse(userPage);
    }

    public Map<String, Object> getAllJoinedUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> userPage = userRepository.findAllByOrderByIdAsc(pageable);
        return buildUserPaginationResponse(userPage);
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

    public DashboardAdminStats getDashboardStats(){
        List<RoleCountDTO> roleCountDTOS = userRepository.countUsersByRole();

        Map<String,Long>roleCountMap=new HashMap<>();
        for (RoleCountDTO dto:roleCountDTOS){
            roleCountMap.put(dto.getRoleName().substring(5),dto.getCount());
        }
        DashboardAdminStats stats=new DashboardAdminStats();
        stats.setRoleCounts(roleCountMap);
        stats.setTotalUsers(userRepository.count());
        stats.setActiveUsers(18L);
        stats.setPendingUsers(5L);
        return stats;
    }

    @Override
    public Page<SellerSummaryDTO> getSellers(Status status, Pageable pageable) {
        Page<Object[]> raw;
        if (status != null && !status.toString().isEmpty()) {
            raw = sellerRepository.findSellerSummariesByStatus(status, pageable);
        } else {
            raw = sellerRepository.findSellerSummaries(pageable);
        }
        List<SellerSummaryDTO> dtos = raw.getContent().stream()
                .map(this::mapRow)
                .toList();
        return new PageImpl<>(dtos, pageable, raw.getTotalElements());
    }

    /** Maps a native query Object[] row → SellerSummaryDTO */
    private SellerSummaryDTO mapRow(Object[] row) {
        // row order must match SELECT column order in the repository query
        return SellerSummaryDTO.builder()
                .id(((Number) row[0]).longValue())
                .username((String) row[1])
                .fullName((String) row[2])
                .profileImgUrl((String) row[3])
                .status((String) row[4])
                .totalProjects(((Number) row[5]).intValue())
                .totalSales(((Number) row[6]).intValue())
                .totalRevenue(((Number) row[7]).doubleValue())
                .averageRating(Math.round(((Number) row[8]).doubleValue() * 10.0) / 10.0)
                .createdAt((LocalDateTime) row[9])
                .build();
    }
}
