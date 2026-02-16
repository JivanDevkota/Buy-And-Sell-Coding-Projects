package com.example.projecthub.dto.projectfile;

import com.example.projecthub.model.FileType;
import com.example.projecthub.model.ProjectFile;
import lombok.Data;

@Data
public class ProjectFileResponse {
    private Long id;
    private FileType fileType;
    private String description;
    private Integer displayOrder;
    private String fileName;
    private String fileUrl;

    public static ProjectFileResponse toDTO(ProjectFile projectFile) {
        if (projectFile == null) {
            return null;
        }
        ProjectFileResponse projectFileResponse=new ProjectFileResponse();
        projectFileResponse.setId(projectFile.getId());
        projectFileResponse.setFileType(projectFile.getFileType());
        projectFileResponse.setDescription(projectFile.getDescription());
        projectFileResponse.setDisplayOrder(projectFile.getDisplayOrder());
        projectFileResponse.setFileName(projectFile.getFileName());
        projectFileResponse.setFileUrl(projectFile.getFileUrl());
        return projectFileResponse;
    }
}
