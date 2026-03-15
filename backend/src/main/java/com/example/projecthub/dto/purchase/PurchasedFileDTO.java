package com.example.projecthub.dto.purchase;

import com.example.projecthub.model.FileType;
import com.example.projecthub.model.ProjectFile;
import lombok.Data;

@Data
public class PurchasedFileDTO {
    private Long fileId;
    private String fileName;
    private String fileSize;
    private FileType fileType;
    private String description;
    private Integer displayOrder;

    public static PurchasedFileDTO toFileDto(ProjectFile file) {
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
