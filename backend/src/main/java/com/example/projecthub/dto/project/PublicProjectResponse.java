package com.example.projecthub.dto.project;

import com.example.projecthub.model.Project;
import com.example.projecthub.model.ProjectStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class PublicProjectResponse extends BaseProjectResponse {
    private ProjectStatus status;

    public static PublicProjectResponse fromEntity(Project project) {
        PublicProjectResponse response = new PublicProjectResponse();
        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setProjectImgUrl(project.getProjectImgUrl());
        response.setPrice(project.getPrice());
        response.setLanguagesName(project.getLanguages().stream()
                .map(lang -> lang.getName())
                .collect(Collectors.toList()));
        response.setCategoryName(project.getCategory().getName());
        response.setStatus(project.getStatus());
        return response;
    }
}
