package com.example.projecthub.service.purchase;

import com.example.projecthub.dto.purchase.PurchaseHistoryResponseDTO;
import com.example.projecthub.dto.purchase.PurchaseRequestDTO;
import com.example.projecthub.dto.purchase.PurchaseResponseDTO;
import com.example.projecthub.exception.AlreadyPurchasedException;
import com.example.projecthub.exception.InsufficientBalanceException;
import com.example.projecthub.model.*;
import com.example.projecthub.repository.ProjectRepository;
import com.example.projecthub.repository.PurchaseRepository;
import com.example.projecthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for managing project purchases.
 * 
 * Handles purchase operations including:
 * - Processing purchase requests
 * - Managing buyer purchase history
 * - Calculating buyer spending statistics
 * - Generating seller sales reports
 * 
 * @author Project Hub Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    /**
     * Processes a purchase request from a buyer.
     *
     * Validates purchase eligibility, checks buyer balance, and creates a purchase record.
     * Automatically transfers funds from buyer to seller.
     *
     * @param buyerId the ID of the buyer initiating the purchase
     * @param purchaseRequestDTO the purchase request containing project ID and payment type
     * @return PurchaseResponseDTO containing purchase details
     * @throws RuntimeException if user or project not found
     * @throws RuntimeException if buyer attempts to purchase own project
     * @throws RuntimeException if project is not approved
     * @throws AlreadyPurchasedException if project already purchased by this buyer
     * @throws InsufficientBalanceException if buyer has insufficient balance
     */
    @Transactional
    public PurchaseResponseDTO purchaseRequest(Long buyerId, PurchaseRequestDTO purchaseRequestDTO) {
        log.info("Processing purchase request for buyer: {} on project: {}", buyerId, purchaseRequestDTO.getProjectId());
        
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found with ID: " + buyerId));

        Project project = projectRepository.findById(purchaseRequestDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + purchaseRequestDTO.getProjectId()));

        // Fetch seller directly to avoid LazyInitializationException
        User seller = userRepository.findById(project.getSeller().getId())
                .orElseThrow(() -> new RuntimeException("Seller not found for project"));

        // Validation: Cannot buy own project
        if (seller.getId().equals(buyerId)) {
            log.warn("Buyer {} attempted to purchase own project {}", buyerId, project.getId());
            throw new RuntimeException("You cannot purchase your own project");
        }

        // Validation: Project must be approved
        if (project.getStatus() != ProjectStatus.APPROVED) {
            log.warn("Project {} is not approved. Current status: {}", project.getId(), project.getStatus());
            throw new RuntimeException("Project is not available for purchase. Current status: " + project.getStatus());
        }

        // Validation: Check if already purchased
        boolean alreadyPurchased = purchaseRepository.existsByBuyerIdAndProjectIdAndStatus(
                buyerId, project.getId(), PurchaseStatus.COMPLETED);
        if (alreadyPurchased) {
            log.warn("Buyer {} has already purchased project {}", buyerId, project.getId());
            throw new AlreadyPurchasedException("You have already purchased this project");
        }

        Double price = project.getPrice();
        
        // Validation: Sufficient balance check
        if (buyer.getBalance() < price) {
            log.warn("Buyer {} insufficient balance. Required: {}, Available: {}", 
                    buyerId, price, buyer.getBalance());
            throw new InsufficientBalanceException("Insufficient balance. Required: " + price + ", Available: " + buyer.getBalance());
        }

        // Update buyer and seller balances
        buyer.deductBalance(price);
        seller.addBalance(price);

        // Create and save purchase record
        Purchase purchase = new Purchase();
        purchase.setBuyer(buyer);
        purchase.setProject(project);
        purchase.setPaidAmount(price);
        purchase.setPaymentType(purchaseRequestDTO.getPaymentType());
        purchase.setStatus(PurchaseStatus.COMPLETED);
        purchase.setTransactionId(UUID.randomUUID().toString());
        
        // Increment project purchase count
        project.increasePurchaseCount();

        // Batch all updates in single transaction
        purchaseRepository.save(purchase);
        userRepository.save(buyer);
        userRepository.save(seller);
        projectRepository.save(project);

        log.info("Purchase completed successfully. Transaction ID: {}, Amount: {}", 
                purchase.getTransactionId(), price);
        
        return PurchaseResponseDTO.toResponseDto(purchase);
    }


    /**
     * Retrieves the purchase history for a specific buyer.
     *
     * Returns all purchases ordered by most recent first.
     * 
     * @param buyerId the ID of the buyer
     * @return list of purchase history DTOs ordered by purchase date (newest first)
     */
    @Transactional(readOnly = true)
    public List<PurchaseHistoryResponseDTO> getPurchaseHistory(Long buyerId) {
        return purchaseRepository.findByBuyerIdOrderByPurchasedAtDesc(buyerId)
                .stream()
                .map(PurchaseHistoryResponseDTO::toHistoryDto)
                .collect(Collectors.toList());
    }


    /**
     * Gets the count of purchased projects for a buyer.
     *
     * @param buyerId the ID of the buyer
     * @return the total number of projects purchased by the buyer
     */
    @Transactional(readOnly = true)
    public long purchasedCount(Long buyerId) {
        return purchaseRepository.countByBuyerId(buyerId);
    }

    /**
     * Calculates the lifetime spending of a buyer.
     *
     * Sums all completed purchases that have not been refunded.
     * Includes validation to ensure buyer exists.
     *
     * @param buyerId the ID of the buyer
     * @return the total amount spent across all completed purchases
     * @throws RuntimeException if buyer not found
     */
    @Transactional(readOnly = true)
    public double getLifeTimeSpend(Long buyerId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found with ID: " + buyerId));
        return purchaseRepository.getLifetimeSpend(buyer, PurchaseStatus.COMPLETED);
    }


    /**
     * Gets today's sales statistics (count and revenue).
     * 
     * Aggregates all completed purchases from the start of today's date.
     * 
     * @return map containing:
     *         - "count": number of completed purchases today
     *         - "revenue": total revenue generated today
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getTodaySales() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long salesCount = purchaseRepository.countByStatusAndPurchasedAtBetween(
                PurchaseStatus.COMPLETED, startOfDay, endOfDay);

        Double totalSales = purchaseRepository.sumRevenueByDateRange(startOfDay, endOfDay);

        Map<String, Object> stats = new HashMap<>();
        stats.put("count", salesCount);
        stats.put("revenue", totalSales != null ? totalSales : 0.0);
        return stats;
    }

    /**
     * Gets this week's sales statistics (count and revenue).
     *
     * Aggregates all completed purchases from Monday to Sunday of the current week.
     *
     * @return map containing:
     *         - "count": number of completed purchases this week
     *         - "revenue": total revenue generated this week
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getWeeklySales() {
        LocalDateTime weekStart = LocalDate.now()
                .with(DayOfWeek.MONDAY)
                .atStartOfDay();
        LocalDateTime weekEnd = weekStart.plusWeeks(1);

        long salesCount = purchaseRepository.countByStatusAndPurchasedAtBetween(
                PurchaseStatus.COMPLETED, weekStart, weekEnd);

        Double totalSales = purchaseRepository.sumRevenueByDateRange(weekStart, weekEnd);

        Map<String, Object> stats = new HashMap<>();
        stats.put("count", salesCount);
        stats.put("revenue", totalSales != null ? totalSales : 0.0);
        return stats;
    }

    /**
     * Gets this month's sales statistics (count and revenue).
     *
     * Aggregates all completed purchases from the first day to the last day of the current month.
     *
     * @return map containing:
     *         - "count": number of completed purchases this month
     *         - "revenue": total revenue generated this month
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getMonthlySales() {
        LocalDateTime monthStart = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay();
        LocalDateTime monthEnd = monthStart.plusMonths(1);

        long salesCount = purchaseRepository.countByStatusAndPurchasedAtBetween(
                PurchaseStatus.COMPLETED, monthStart, monthEnd);

        Double totalSales = purchaseRepository.sumRevenueByDateRange(monthStart, monthEnd);

        Map<String, Object> stats = new HashMap<>();
        stats.put("count", salesCount);
        stats.put("revenue", totalSales != null ? totalSales : 0.0);
        return stats;
    }

}
