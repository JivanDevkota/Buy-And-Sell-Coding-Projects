package com.example.projecthub.service.project;

import com.example.projecthub.dto.project.ProjectDTO;
import com.example.projecthub.model.FileType;
import com.example.projecthub.model.Project;
import com.example.projecthub.model.ProjectFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProjectService {

    //Create Project
    ProjectDTO createProject(ProjectDTO projectDTO, MultipartFile file) throws IOException;

    //getAllProject
    //deleteProject

    //getProjectByLanguages
    List<Project> getProjectsByLanguageId(Long languageId);
    Page<Project> getTopProjectsByLanguage(Long languageId, Pageable pageable);
    //getProjectByCategory

    // Add File to Project
    ProjectFile addFileToProject(Long projectId, MultipartFile file,
                                 FileType fileType, String description,
                                 Integer displayOrder) throws IOException;
}
