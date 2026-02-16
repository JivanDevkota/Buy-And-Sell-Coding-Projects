package com.example.projecthub.dto.project;

import com.example.projecthub.dto.projectfile.ProjectFileResponse;
import com.example.projecthub.model.Language;
import com.example.projecthub.model.Project;
import com.example.projecthub.model.ProjectFile;
import com.example.projecthub.model.ProjectStatus;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ProjectDetailsResponse {
    private Long id;
    private String title;
    private String description;
    private String projectImgUrl;
    private Set<String> tags;
    private Double price;
    private List<String> languagesName;
    private ProjectStatus status;
    private String categoryName;

    private List<ProjectFileResponse>files;


    public static ProjectDetailsResponse toDTO(Project project) {
     ProjectDetailsResponse dto = new ProjectDetailsResponse();
     dto.setId(project.getId());
     dto.setTitle(project.getTitle());
     dto.setDescription(project.getDescription());
     dto.setProjectImgUrl(project.getProjectImgUrl());
     dto.setTags(project.getTags());
     dto.setPrice(project.getPrice());
     dto.setLanguagesName(project.getLanguages().stream().map(Language::getName).collect(Collectors.toList()));
     dto.setStatus(project.getStatus());
     dto.setCategoryName(project.getCategory().getName());

     dto.setFiles(project.getProjectFiles().stream()
             .sorted(Comparator.comparing(ProjectFile::getDisplayOrder).reversed())
             .map(ProjectFileResponse::toDTO)
             .collect(Collectors.toList()));
     return dto;
    }

}
