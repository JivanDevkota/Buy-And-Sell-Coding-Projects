package com.example.projecthub.service.seller;


import com.example.projecthub.dto.dashboard.DashboardResponse;
import com.example.projecthub.repository.ProjectRepository;
import com.example.projecthub.repository.PurchaseRepository;
import com.example.projecthub.repository.ReviewRepository;
import com.example.projecthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service implementation for managing sellers.
 * Handles seller-related operations including listing and filtering.
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SellerServiceImpl implements SellerService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PurchaseRepository purchaseRepository;
    private final ReviewRepository reviewRepository;

    public DashboardResponse getSellerDashboardResponse(Long sellerId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();

        LocalDateTime startOfLastMonth = startOfMonth.minusMonths(1);
        LocalDateTime endOfLastMonth = startOfMonth.minusSeconds(1);

        //current
        Double earnings = purchaseRepository.getTotalEarningBySeller(sellerId);
        Long sales = purchaseRepository.getTotalSalesBySeller(sellerId);
        Long activeProjects = projectRepository.getActiveProjectsBySeller(sellerId);
        Double rating = reviewRepository.getAverageRatingBySeller(sellerId);

        //previous
        Double lastMonthEarnings = purchaseRepository.getEarningsBetweenBySeller(sellerId, startOfLastMonth, endOfLastMonth);
        Long lastMonthSales = purchaseRepository.getSalesBetweenBySeller(sellerId, startOfLastMonth, endOfLastMonth);
        Double lastMonthReview = reviewRepository.getAverageRatingBetweenBySeller(sellerId, startOfLastMonth, endOfLastMonth);

        //trend
        double earningsChange = calculatePercentage(earnings, lastMonthEarnings != null ? lastMonthEarnings : 0);
        double salesChange = calculatePercentage(sales.doubleValue(), lastMonthSales != null ? lastMonthSales.doubleValue() : 0);
        double ratingChange = calculatePercentage(rating, lastMonthReview != null ? lastMonthReview : 0);
        return new DashboardResponse(earnings, earningsChange, sales, salesChange, activeProjects, rating, ratingChange);
    }


    private double calculatePercentage(double current, double previous) {
        if (previous == 0) return 0;
//                current > 0 ? 100 : 0;
        return ((current - previous) / previous) * 100;
    }


}
