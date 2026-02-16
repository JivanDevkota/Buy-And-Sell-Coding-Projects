package com.example.projecthub.service.project;

import com.example.projecthub.dto.project.*;
import com.example.projecthub.dto.projectfile.ProjectFileDTO;
import com.example.projecthub.model.FileType;
import com.example.projecthub.model.Project;
import com.example.projecthub.model.ProjectFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProjectService {


    //getAllProject
    //deleteProject

    //getProjectByLanguages
    List<Project> getProjectsByLanguageId(Long languageId);

    Page<Project> getTopProjectsByLanguage(Long languageId, Pageable pageable);
    //getProjectByCategory

    /**
     * Creates a new project
     * @param projectDTO project data
     * @param file project image file
     * @return created project DTO
     * @throws IOException if file upload fails
     */
    ProjectDTO createProject(ProjectDTO projectDTO, MultipartFile file) throws IOException;

    /**
     * Adds a file to an existing project
     * @param projectFileDTO file metadata
     * @param file the file to upload
     * @param userId the user ID performing the action
     * @return created project file DTO
     * @throws IOException if file upload fails
     */
    ProjectFileDTO addFileToProject(ProjectFileDTO projectFileDTO, MultipartFile file, Long userId) throws IOException;

    List<ProjectResponse> getMyProjects(Long userId);

    ProjectDetailsResponse getProjectDetailsById(Long projectId);

    List<PublicProjectResponse>getAllProjects();

    PublicProjectDetailsResponse getProjectDetails(Long projectId);


    ProjectResponse submitForReview(Long projectId);
    ProjectResponse withdrawFromReview(Long projectId);
    ProjectResponse approveProject(Long projectId);
    ProjectResponse rejectProject(Long projectId);
    ProjectResponse suspendProject(Long projectId);
}
