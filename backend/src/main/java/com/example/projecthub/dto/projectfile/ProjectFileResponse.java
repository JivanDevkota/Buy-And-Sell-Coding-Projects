package com.example.projecthub.dto.projectfile;

import com.example.projecthub.model.ProjectFile;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectFileResponse extends ProjectFileDTO {

    public static ProjectFileResponse fromEntity(ProjectFile projectFile) {
        if (projectFile == null) {
            return null;
        }
        ProjectFileResponse response = new ProjectFileResponse();
        response.setId(projectFile.getId());
        response.setProjectId(projectFile.getProject() != null ? projectFile.getProject().getId() : null);
        response.setFileType(projectFile.getFileType());
        response.setDescription(projectFile.getDescription());
        response.setDisplayOrder(projectFile.getDisplayOrder());
        response.setFileName(projectFile.getFileName());
        response.setFileUrl(projectFile.getFileUrl());
        return response;
    }
}
