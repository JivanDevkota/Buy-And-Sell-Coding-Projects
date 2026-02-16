package com.example.projecthub.dto.project;

import com.example.projecthub.model.Language;
import com.example.projecthub.model.Project;
import com.example.projecthub.model.ProjectStatus;
import lombok.Data;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ProjectResponse {
    private Long id;
    private String title;
    private String description;
    private String projectImgUrl;
    private Set<String> tags;
    private Double price;
    private List<String> languagesName;
    private ProjectStatus status;
    private String categoryName;

    public static ProjectResponse toProjectDto(Project project) {
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getId());
        projectResponse.setTitle(project.getTitle());
        projectResponse.setDescription(project.getDescription());
        projectResponse.setProjectImgUrl(project.getProjectImgUrl());
        projectResponse.setTags(project.getTags());
        projectResponse.setPrice(project.getPrice());
        projectResponse.setStatus(project.getStatus());
        projectResponse.setLanguagesName(project.getLanguages().stream()
                .map(Language::getName)
                .collect(Collectors.toList()));
        projectResponse.setCategoryName(project.getCategory().getName());
        return projectResponse;
    }
}

