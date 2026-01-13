package com.example.projecthub.dto.purchase;

import com.example.projecthub.model.PaymentType;
import com.example.projecthub.model.Purchase;
import com.example.projecthub.model.PurchaseStatus;
import lombok.Data;

@Data
public class PurchaseDTO {
    private Long id;
    private Long customerId;
    private Long projectId;
    private PaymentType paymentType;
    private PurchaseStatus purchaseStatus;


    public static PurchaseDTO toDTO(Purchase purchase) {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(purchase.getId());
        dto.setCustomerId(purchase.getBuyer().getId());
        dto.setProjectId(purchase.getProject().getId());
        dto.setPaymentType(purchase.getPaymentType());
        dto.setPurchaseStatus(purchase.getStatus());
        return dto;
    }
}
