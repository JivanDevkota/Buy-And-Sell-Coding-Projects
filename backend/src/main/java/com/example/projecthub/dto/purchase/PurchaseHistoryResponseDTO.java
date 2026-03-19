package com.example.projecthub.dto.purchase;

import com.example.projecthub.model.Purchase;
import com.example.projecthub.model.PurchaseStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PurchaseHistoryResponseDTO {
    private Long id;
    private String projectTitle;
    private String projectDescription;
    private String projectImgUrl;
    private String sellerUsername;
    private Double paidAmount;
    private PurchaseStatus status;
    private LocalDateTime purchasedAt;
    private Boolean isRefunded;
    private Boolean canDownload;
    private List<PurchasedFileDTO> files;
    private Long projectId;


    public static PurchaseHistoryResponseDTO toHistoryDto(Purchase purchase) {
        PurchaseHistoryResponseDTO dto = new PurchaseHistoryResponseDTO();
        dto.setId(purchase.getId());
        dto.setProjectTitle(purchase.getProject().getTitle());
        dto.setProjectDescription(purchase.getProject().getDescription());
        dto.setProjectImgUrl(purchase.getProject().getProjectImgUrl());
        dto.setSellerUsername(purchase.getProject().getSeller().getUsername());
        dto.setPaidAmount(purchase.getPaidAmount());
        dto.setStatus(purchase.getStatus());
        dto.setPurchasedAt(purchase.getPurchasedAt());
        dto.setIsRefunded(purchase.getRefunded());
        dto.setCanDownload(purchase.canDownload());
        dto.setProjectId(purchase.getProject().getId());

        if (purchase.canDownload()) {
            List<PurchasedFileDTO> files = purchase.getProject().getProjectFiles()
                    .stream()
                    .filter(f -> f.getIsActive())
                    .map(PurchasedFileDTO::toFileDto)
                    .collect(Collectors.toList());
            dto.setFiles(files);
        }
        return dto;
    }
}
