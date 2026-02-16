package com.example.projecthub.service.project;

import com.example.projecthub.dto.project.*;
import com.example.projecthub.dto.projectfile.ProjectFileDTO;
import com.example.projecthub.helper.FileHelper;
import com.example.projecthub.helper.ImageHelper;
import com.example.projecthub.model.*;
import com.example.projecthub.repository.*;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final ImageHelper imageHelper;
    private final FileHelper fileHelper; // ✅ Add FileHelper
    private final ProjectFileRepository projectFileRepository;

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO, MultipartFile file) throws IOException {
        log.info("Creating project with title: {} for user ID: {}", projectDTO.getTitle(), projectDTO.getUserId());

        // Validate file
        if (file == null || file.isEmpty()) {
            throw new ValidationException("Project image file is required");
        }

        // Fetch user
        User user = userRepository.findById(projectDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + projectDTO.getUserId()));

        // Fetch category
        Category category = categoryRepository.findById(projectDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + projectDTO.getCategoryId()));

        // Fetch and validate languages
        Set<Language>languages=new HashSet<>( languageRepository.findAllById(projectDTO.getLanguageIds()));
        if (languages.isEmpty()) {
            throw new ValidationException("At least one valid language is required");
        }
        if (languages.size() != projectDTO.getLanguageIds().size()) {
            throw new ValidationException("Some language IDs are invalid");
        }

        // Upload image (use ImageHelper for project thumbnail)
        String imgUrl = imageHelper.uploadImage(file);

        // Create project
        Project project = new Project();
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setTags(new HashSet<>(projectDTO.getTags()));
        project.setPrice(projectDTO.getPrice());
        project.setCategory(category);
        project.setLanguages(languages);
        project.setSeller(user);
        project.setStatus(ProjectStatus.DRAFT);
        project.setProjectImgUrl(imgUrl);
        project.setProjectFiles(new ArrayList<>());
        project.setReviews(new ArrayList<>());
        project.setPurchases(new ArrayList<>());

        Project savedProject = projectRepository.save(project);
        log.info("Project created successfully with ID: {}", savedProject.getId());

        return ProjectDTO.toDTO(savedProject);
    }

    @Override
    @Transactional
    public ProjectFileDTO addFileToProject(ProjectFileDTO projectFileDTO, MultipartFile file, Long userId) throws IOException {
        log.info("Adding file to project ID: {} by user ID: {}", projectFileDTO.getProjectId(), userId);

        // Validate file
        if (file == null || file.isEmpty()) {
            throw new ValidationException("File is required");
        }

        // Fetch project and verify ownership
        Project project = projectRepository.findById(projectFileDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectFileDTO.getProjectId()));

        if (!project.getSeller().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to add files to this project");
        }

        // Determine display order
        Integer displayOrder = projectFileDTO.getDisplayOrder();
        if (displayOrder == null) {
            displayOrder = projectFileRepository.findMaxDisplayOrderByProjectId(projectFileDTO.getProjectId())
                    .map(max -> max + 1)
                    .orElse(0);
        }

        // ✅ Use FileHelper instead of ImageHelper for project files
        String fileUrl = fileHelper.uploadFile(file);

        // Create project file
        ProjectFile projectFile = new ProjectFile();
        projectFile.setFileName(file.getOriginalFilename());
        projectFile.setFileUrl(fileUrl);
        projectFile.setFileType(projectFileDTO.getFileType());
        projectFile.setDescription(projectFileDTO.getDescription());
        projectFile.setDisplayOrder(displayOrder);
        projectFile.setProject(project);

        ProjectFile savedFile = projectFileRepository.save(projectFile);
        log.info("File added successfully to project ID: {}", projectFileDTO.getProjectId());

        return ProjectFileDTO.toDTO(savedFile);
    }

    public List<PublicProjectResponse>getAllProjects(){
       return projectRepository.findAllWithLanguagesAndCategory()
               .stream()
               .limit(6)
               .map(PublicProjectResponse::toProjectDto)
               .collect(Collectors.toList());
    }

    public PublicProjectDetailsResponse getProjectDetails(Long projectId) {

        Project project = projectRepository.findProjectDetailsById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found with id: " + projectId)
                );

        return PublicProjectDetailsResponse.toDTO(project);
    }

    public List<Project> getProjectsByLanguageId(Long languageId) {
        return projectRepository.findByLanguages_Id(languageId);
    }

    public Page<Project> getTopProjectsByLanguage(Long languageId, Pageable pageable) {
        log.info("Fetching top projects by language ID: {} with pageable: {}", languageId, pageable);

        return projectRepository.findTopByLanguage(languageId, pageable);
    }

    public List<ProjectResponse> getMyProjects(Long userId) {
        List<Project> projects = projectRepository.findAllBySellerId(userId);
        return projects.stream()
                .map(ProjectResponse::toProjectDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectDetailsResponse getProjectDetailsById(Long projectId) {
        Project project = projectRepository
                .findByIdWithLanguages(projectId).orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
        return ProjectDetailsResponse.toDTO(project);
    }

    @Transactional
    public ProjectResponse submitForReview(Long projectId) {
        log.info("Submitting projectId: {} for reviews", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        //validate project has required information
        if (project.getProjectFiles() == null || project.getProjectFiles().isEmpty()) {
            throw new ValidationException("Cannot submit project without projectFiles. Please add atleast one project file");
        }

        if (project.getStatus() != ProjectStatus.DRAFT && project.getStatus() != ProjectStatus.REJECTED) {
            throw new ValidationException("Only DRAFT and REJECTED project can be submitted for reviews .");
        }

        project.setStatus(ProjectStatus.UNDER_REVIEW);
        Project updateProject = projectRepository.save(project);
        log.info("Project Id: {} submitted for review successfully", projectId);
        return ProjectResponse.toProjectDto(updateProject);
    }

    @Transactional
    public ProjectResponse withdrawFromReview(Long projectId) {
        log.info("Withdrawn Project ID: {} from review", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
        if (project.getStatus() != ProjectStatus.UNDER_REVIEW) {
            throw new ValidationException("Only UNDER_REVIEW project can be withdrawn .");
        }
        project.setStatus(ProjectStatus.DRAFT);
        Project updateProject = projectRepository.save(project);
        log.info("Project ID: {} withdrawn successfully", projectId);
        return ProjectResponse.toProjectDto(updateProject);
    }

    @Transactional
    public ProjectResponse approveProject(Long projectId) {
        log.info("Approving project ID: {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
        if (project.getStatus()!=ProjectStatus.UNDER_REVIEW){
            throw new ValidationException("Only UNDER_REVIEW project can be approved .");
        }
        project.setStatus(ProjectStatus.APPROVED);
        project.setIsActive(true);
        Project updateProject = projectRepository.save(project);
        log.info("Project ID: {} approved successfully", projectId);
        return ProjectResponse.toProjectDto(updateProject);
    }

    @Transactional
    public ProjectResponse rejectProject(Long projectId) {
        log.info("Rejecting project ID: {}", projectId);
        Project project = projectRepository
                .findById(projectId).orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
        if (project.getStatus()!=ProjectStatus.UNDER_REVIEW){
            throw new ValidationException("Only UNDER_REVIEW project can be rejected .");
        }
        project.setStatus(ProjectStatus.REJECTED);
        project.setIsActive(false);
        Project updateProject = projectRepository.save(project);
        log.info("Project ID: {} rejected successfully", projectId);
        return ProjectResponse.toProjectDto(updateProject);
    }

    @Transactional
    public ProjectResponse suspendProject(Long projectId) {
        log.info("Suspending project ID: {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
        if (project.getStatus()!=ProjectStatus.APPROVED){
            throw new ValidationException("Only APPROVED project can be suspended .");
        }
        project.setStatus(ProjectStatus.SUSPENDED);
        project.setIsActive(false);
        Project updateProject = projectRepository.save(project);
        log.info("Project ID: {} suspended successfully", projectId);
        return ProjectResponse.toProjectDto(updateProject);
    }
}