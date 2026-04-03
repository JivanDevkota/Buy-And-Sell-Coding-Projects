package com.example.projecthub.service.admin;

import com.example.projecthub.dto.buyer.BuyerSummaryDTO;
import com.example.projecthub.dto.category.CategoryDTO;
import com.example.projecthub.dto.dashboard.DashboardAdminStats;
import com.example.projecthub.dto.seller.PagedResponse;
import com.example.projecthub.dto.seller.PendingSellerApprovals;
import com.example.projecthub.dto.seller.SellerSummaryDTO;
import com.example.projecthub.model.Category;
import com.example.projecthub.model.Status;
import org.springframework.data.domain.Pageable;

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

    DashboardAdminStats getDashboardStats();

    PagedResponse<SellerSummaryDTO> getSellers(Status status, Pageable pageable);

    List<BuyerSummaryDTO> getTop5Buyers();

    Long countSellerByStatus();
    Long countProjectByStatus();

    List<PendingSellerApprovals>getAllSellerPendingApprovals();
}
