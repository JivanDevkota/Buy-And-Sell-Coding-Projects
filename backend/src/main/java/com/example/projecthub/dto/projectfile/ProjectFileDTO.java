package com.example.projecthub.dto.projectfile;

import com.example.projecthub.model.FileType;
import com.example.projecthub.model.ProjectFile;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFileDTO {

    private Long id;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "File type is required")
    private FileType fileType;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @PositiveOrZero(message = "Display order must be zero or positive")
    private Integer displayOrder;

    private String fileName;

    private String fileUrl;

    /**
     * Converts ProjectFile entity to DTO
     * @param projectFile the entity
     * @return ProjectFileDTO instance
     */
    public static ProjectFileDTO toDTO(ProjectFile projectFile) {
        if (projectFile == null) {
            return null;
        }

        ProjectFileDTO dto = new ProjectFileDTO();
        dto.setId(projectFile.getId());
        dto.setProjectId(projectFile.getProject() != null ? projectFile.getProject().getId() : null);
        dto.setFileType(projectFile.getFileType());
        dto.setDescription(projectFile.getDescription());
        dto.setDisplayOrder(projectFile.getDisplayOrder());
        dto.setFileName(projectFile.getFileName());
        dto.setFileUrl(projectFile.getFileUrl());

        return dto;
    }
}