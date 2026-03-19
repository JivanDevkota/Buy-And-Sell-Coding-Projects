package com.example.projecthub.service.project;

import com.example.projecthub.dto.project.*;
import com.example.projecthub.dto.projectfile.ProjectFileDTO;
import com.example.projecthub.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProjectService {


    List<Project> getProjectsByLanguageId(Long languageId);

    Page<Project> getTopProjectsByLanguage(Long languageId, Pageable pageable);


    ProjectDTO createProject(ProjectDTO projectDTO, MultipartFile file) throws IOException;

    ProjectFileDTO addFileToProject(ProjectFileDTO projectFileDTO, MultipartFile file, Long userId) throws IOException;

    List<ProjectResponse> getMyProjects(Long userId);

    ProjectDetailsResponse getProjectDetailsById(Long projectId);

    List<PublicProjectResponse> getAllProjects();

    PublicProjectDetailsResponse getProjectDetails(Long projectId);


    ProjectResponse submitForReview(Long projectId);

    ProjectResponse withdrawFromReview(Long projectId);

    ProjectResponse approveProject(Long projectId);

    ProjectResponse rejectProject(Long projectId);

    ProjectResponse suspendProject(Long projectId);

    Page<PendingProjects> getUnderReviewProjects(int page, int size);
}
