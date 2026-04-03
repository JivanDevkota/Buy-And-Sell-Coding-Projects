package com.example.projecthub.dto.project;

import com.example.projecthub.dto.projectfile.ProjectFileResponse;
import com.example.projecthub.model.Project;
import com.example.projecthub.model.ProjectFile;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class PublicProjectDetailsResponse extends PublicProjectResponse {
    private List<ProjectFileResponse> files;

    public static PublicProjectDetailsResponse fromEntity(Project project) {
        PublicProjectDetailsResponse response = new PublicProjectDetailsResponse();
        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setProjectImgUrl(project.getProjectImgUrl());
        response.setTags(project.getTags());
        response.setPrice(project.getPrice());
        response.setLanguagesName(project.getLanguages().stream()
                .map(lang -> lang.getName())
                .collect(Collectors.toList()));
        response.setCategoryName(project.getCategory().getName());
        response.setStatus(project.getStatus());

        response.setFiles(project.getProjectFiles().stream()
                .sorted(Comparator.comparing(
                        ProjectFile::getDisplayOrder,
                        Comparator.nullsLast(Integer::compareTo)
                ).reversed())
                .map(ProjectFileResponse::fromEntity)
                .collect(Collectors.toList()));
        return response;
    }
}
