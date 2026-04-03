package com.example.projecthub.dto.purchase;

import com.example.projecthub.model.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PurchaseDTO {
    private Long id;
    private Long buyerId;
    private String buyerUsername;
    private Long projectId;
    private String projectTitle;
    private String projectDescription;
    private String projectImgUrl;
    private String sellerUsername;
    private Double paidAmount;
    private PaymentType paymentType;
    private PurchaseStatus status;
    private String transactionId;
    private LocalDateTime purchasedAt;
    private Boolean isRefunded;
    private LocalDateTime refundedAt;
    private String refundReason;
    private Boolean canDownload;
    private List<PurchasedFileDTO> files;

    public static PurchaseDTO fromEntity(Purchase purchase) {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(purchase.getId());
        dto.setBuyerId(purchase.getBuyer().getId());
        dto.setBuyerUsername(purchase.getBuyer().getUsername());
        dto.setProjectId(purchase.getProject().getId());
        dto.setProjectTitle(purchase.getProject().getTitle());
        dto.setProjectDescription(purchase.getProject().getDescription());
        dto.setProjectImgUrl(purchase.getProject().getProjectImgUrl());
        dto.setSellerUsername(purchase.getProject().getSeller().getUsername());
        dto.setPaidAmount(purchase.getPaidAmount());
        dto.setPaymentType(purchase.getPaymentType());
        dto.setStatus(purchase.getStatus());
        dto.setTransactionId(purchase.getTransactionId());
        dto.setPurchasedAt(purchase.getPurchasedAt());
        dto.setIsRefunded(purchase.getRefunded());
        dto.setRefundedAt(purchase.getRefundedAt());
        dto.setRefundReason(purchase.getRefundReason());
        dto.setCanDownload(purchase.canDownload());

        if (purchase.canDownload()) {
            dto.setFiles(purchase.getProject().getProjectFiles()
                    .stream()
                    .filter(f -> f.getIsActive())
                    .map(PurchasedFileDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    @Data
    public static class PurchasedFileDTO {
        private Long fileId;
        private String fileName;
        private String fileSize;
        private FileType fileType;
        private String description;
        private Integer displayOrder;

        public static PurchasedFileDTO fromEntity(ProjectFile file) {
            PurchasedFileDTO dto = new PurchasedFileDTO();
            dto.setFileId(file.getId());
            dto.setFileName(file.getFileName());
            dto.setFileSize(file.getFileSize());
            dto.setFileType(file.getFileType());
            dto.setDescription(file.getDescription());
            dto.setDisplayOrder(file.getDisplayOrder());
            return dto;
        }
    }
}
