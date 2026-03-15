package com.example.projecthub.dto.purchase;

import com.example.projecthub.model.PaymentType;
import com.example.projecthub.model.Purchase;
import com.example.projecthub.model.PurchaseStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchaseResponseDTO {
    private Long id;
    private Long buyerId;
    private String buyerUsername;
    private Long projectId;
    private String projectTitle;
    private String projectImgUrl;
    private Double paidAmount;
    private PaymentType paymentType;
    private PurchaseStatus status;
    private String transactionId;
    private LocalDateTime purchasedAt;
    private Boolean isRefunded;
    private LocalDateTime refundedAt;
    private String refundReason;

    public static PurchaseResponseDTO toResponseDto(Purchase purchase) {
        PurchaseResponseDTO dto = new PurchaseResponseDTO();
        dto.setId(purchase.getId());
        dto.setBuyerId(purchase.getBuyer().getId());
        dto.setBuyerUsername(purchase.getBuyer().getUsername());
        dto.setProjectId(purchase.getProject().getId());
        dto.setProjectTitle(purchase.getProject().getTitle());
        dto.setProjectImgUrl(purchase.getProject().getProjectImgUrl());
        dto.setPaidAmount(purchase.getPaidAmount());
        dto.setPaymentType(purchase.getPaymentType());
        dto.setStatus(purchase.getStatus());
        dto.setTransactionId(purchase.getTransactionId());
        dto.setPurchasedAt(purchase.getPurchasedAt());
        dto.setIsRefunded(purchase.getRefunded());
        dto.setRefundedAt(purchase.getRefundedAt());
        dto.setRefundReason(purchase.getRefundReason());
        return dto;
    }

}
