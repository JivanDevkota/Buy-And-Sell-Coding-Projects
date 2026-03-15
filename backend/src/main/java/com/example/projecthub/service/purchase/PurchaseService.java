package com.example.projecthub.service.purchase;

import com.example.projecthub.dto.purchase.PurchaseHistoryResponseDTO;
import com.example.projecthub.dto.purchase.PurchaseRequestDTO;
import com.example.projecthub.dto.purchase.PurchaseResponseDTO;

import java.util.List;
import java.util.Map;

public interface PurchaseService {
    PurchaseResponseDTO purchaseRequest(Long buyerId, PurchaseRequestDTO purchaseRequestDTO);
    List<PurchaseHistoryResponseDTO> getPurchaseHistory(Long buyerId);
    long purchasedCount(Long buyerId);
    double getLifeTimeSpend(Long buyerId);


    Map<String,Object> getTodaySales();
    Map<String,Object>getWeeklySales();
    Map<String,Object>getMonthlySales();
}
