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
public class PublicProjectDetailsResponse {

    private Long id;
    private String title;
    private String description;
    private String projectImgUrl;
    private Set<String> tags;
    private Double price;
    private List<String> languagesName;
    private String categoryName;


    public static PublicProjectDetailsResponse toDTO(Project project) {
        PublicProjectDetailsResponse dto = new PublicProjectDetailsResponse();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setProjectImgUrl(project.getProjectImgUrl());
        dto.setTags(project.getTags());
        dto.setPrice(project.getPrice());
        dto.setLanguagesName(project.getLanguages().stream().map(Language::getName).collect(Collectors.toList()));
        dto.setCategoryName(project.getCategory().getName());
        return dto;
    }
}
