package com.example.projecthub.dto.purchase;

import com.example.projecthub.model.PaymentType;
import com.example.projecthub.model.Purchase;
import com.example.projecthub.model.PurchaseStatus;
import lombok.Data;

@Data
public class PurchaseRequestDTO {
    private Long projectId;
    private PaymentType paymentType;
}
