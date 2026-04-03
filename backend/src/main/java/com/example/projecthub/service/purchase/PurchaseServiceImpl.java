package com.example.projecthub.service.purchase;

import com.example.projecthub.dto.purchase.PurchaseHistoryResponseDTO;
import com.example.projecthub.dto.purchase.PurchaseRequestDTO;
import com.example.projecthub.dto.purchase.PurchaseResponseDTO;
import com.example.projecthub.exception.AlreadyPurchasedException;
import com.example.projecthub.exception.InsufficientBalanceException;
import com.example.projecthub.exception.ResourceNotFoundException;
import com.example.projecthub.model.*;
import com.example.projecthub.repository.ProjectRepository;
import com.example.projecthub.repository.PurchaseRepository;
import com.example.projecthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {

    private static final String BUYER_NOT_FOUND = "Buyer not found with ID: ";
    private static final String PROJECT_NOT_FOUND = "Project not found with ID: ";
    private static final String SELLER_NOT_FOUND = "Seller not found for project";
    private static final String OWN_PROJECT_PURCHASE = "You cannot purchase your own project";
    private static final String PROJECT_NOT_APPROVED = "Project is not available for purchase. Current status: ";
    private static final String ALREADY_PURCHASED = "You have already purchased this project";
    private static final String INSUFFICIENT_BALANCE = "Insufficient balance. Required: {}, Available: {}";

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    @Transactional
    public PurchaseResponseDTO purchaseRequest(Long buyerId, PurchaseRequestDTO purchaseRequestDTO) {
        log.info("Processing purchase for buyer: {} on project: {}", buyerId, purchaseRequestDTO.getProjectId());

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException(BUYER_NOT_FOUND + buyerId));

        Project project = projectRepository.findById(purchaseRequestDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND + purchaseRequestDTO.getProjectId()));

        User seller = userRepository.findById(project.getSeller().getId())
                .orElseThrow(() -> new ResourceNotFoundException(SELLER_NOT_FOUND));

        if (seller.getId().equals(buyerId)) {
            log.warn("Buyer {} attempted to purchase own project {}", buyerId, project.getId());
            throw new IllegalArgumentException(OWN_PROJECT_PURCHASE);
        }

        if (project.getStatus() != ProjectStatus.APPROVED) {
            log.warn("Project {} is not approved. Status: {}", project.getId(), project.getStatus());
            throw new IllegalArgumentException(PROJECT_NOT_APPROVED + project.getStatus());
        }

        if (purchaseRepository.existsByBuyerIdAndProjectIdAndStatus(buyerId, project.getId(), PurchaseStatus.COMPLETED)) {
            log.warn("Buyer {} already purchased project {}", buyerId, project.getId());
            throw new AlreadyPurchasedException(ALREADY_PURCHASED);
        }

        double price = project.getPrice();
        if (buyer.getBalance() < price) {
            log.warn("Insufficient balance for buyer {}. Required: {}, Available: {}", buyerId, price, buyer.getBalance());
            throw new InsufficientBalanceException(String.format(INSUFFICIENT_BALANCE, price, buyer.getBalance()));
        }

        buyer.deductBalance(price);
        seller.addBalance(price);

        Purchase purchase = new Purchase();
        purchase.setBuyer(buyer);
        purchase.setProject(project);
        purchase.setPaidAmount(price);
        purchase.setPaymentType(purchaseRequestDTO.getPaymentType());
        purchase.setStatus(PurchaseStatus.COMPLETED);
        purchase.setTransactionId(UUID.randomUUID().toString());
        project.increasePurchaseCount();

        purchaseRepository.save(purchase);
        userRepository.save(buyer);
        userRepository.save(seller);
        projectRepository.save(project);

        log.info("Purchase completed. Transaction ID: {}, Amount: {}", purchase.getTransactionId(), price);
        return PurchaseResponseDTO.toResponseDto(purchase);
    }


    @Transactional(readOnly = true)
    public List<PurchaseHistoryResponseDTO> getPurchaseHistory(Long buyerId) {
        log.debug("Fetching purchase history for buyer: {}", buyerId);
        return purchaseRepository.findByBuyerIdOrderByPurchasedAtDesc(buyerId)
                .stream()
                .map(PurchaseHistoryResponseDTO::toHistoryDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public long purchasedCount(Long buyerId) {
        log.debug("Counting purchases for buyer: {}", buyerId);
        return purchaseRepository.countByBuyerId(buyerId);
    }

    @Transactional(readOnly = true)
    public double getLifeTimeSpend(Long buyerId) {
        log.debug("Calculating lifetime spend for buyer: {}", buyerId);
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException(BUYER_NOT_FOUND + buyerId));
        return purchaseRepository.getLifetimeSpend(buyer, PurchaseStatus.COMPLETED);
    }


    @Transactional(readOnly = true)
    public Map<String, Object> getTodaySales(long sellerId) {
        log.debug("Fetching today's sales for seller: {}", sellerId);
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return getSalesStats(sellerId, startOfDay, endOfDay);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getWeeklySales(long sellerId) {
        log.debug("Fetching weekly sales for seller: {}", sellerId);
        LocalDateTime weekStart = LocalDate.now()
                .with(DayOfWeek.MONDAY)
                .atStartOfDay();
        LocalDateTime weekEnd = weekStart.plusWeeks(1);
        return getSalesStats(sellerId, weekStart, weekEnd);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMonthlySales(long sellerId) {
        log.debug("Fetching monthly sales for seller: {}", sellerId);
        LocalDateTime monthStart = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay();
        LocalDateTime monthEnd = monthStart.plusMonths(1);
        return getSalesStats(sellerId, monthStart, monthEnd);
    }

    private Map<String, Object> getSalesStats(long sellerId, LocalDateTime startDate, LocalDateTime endDate) {
        long salesCount = purchaseRepository.countSellerSalesByDateRange(sellerId, startDate, endDate);
        Double totalRevenue = purchaseRepository.sumSellerRevenueByDateRange(sellerId, startDate, endDate);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", salesCount);
        stats.put("revenue", totalRevenue != null ? totalRevenue : 0.0);
        return stats;
    }

}
