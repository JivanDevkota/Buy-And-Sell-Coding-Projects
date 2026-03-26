package com.example.projecthub.service.seller;

import com.example.projecthub.dto.dashboard.DashboardResponse;

public interface SellerService {
    DashboardResponse getSellerDashboardResponse(Long sellerId);
}
