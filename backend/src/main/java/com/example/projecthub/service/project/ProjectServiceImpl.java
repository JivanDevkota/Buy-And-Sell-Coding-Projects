package com.example.projecthub.service.project;

import com.example.projecthub.dto.project.ProjectDTO;
import com.example.projecthub.helper.ImageHelper;
import com.example.projecthub.model.*;
import com.example.projecthub.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final ImageHelper imageHelper;
    private final ProjectFileRepository projectFileRepository;

    public ProjectDTO createProject(ProjectDTO projectDTO, MultipartFile file) throws IOException {

        User user = userRepository.findById(projectDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("user not found"));

        Category category = categoryRepository.findById(projectDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("category not found"));

        List<Language> languages = languageRepository.findAllById(projectDTO.getLanguages());

        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setTags(projectDTO.getTags());
        project.setPrice(projectDTO.getPrice());
        project.setCategory(category);
        project.setLanguages(languages);
        project.setSeller(user);
        project.setStatus(ProjectStatus.DRAFT);
        String imgUrl = imageHelper.uploadImage(file);
        project.setProjectImgUrl(imgUrl);
        Project savedProject = projectRepository.save(project);
        return ProjectDTO.toDTO(savedProject);
    }

    public ProjectFile addFileToProject(Long projectId, MultipartFile file,
                                        FileType fileType, String description, Integer displayOrder) throws IOException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("project not found"));
        String savedImg = imageHelper.uploadImage(file);
        ProjectFile projectFile = new ProjectFile();
        projectFile.setFileName(file.getOriginalFilename());
        projectFile.setFileUrl(savedImg);
        projectFile.setFileType(fileType);
        projectFile.setDescription(description);

        if (displayOrder == null) {
            int maxOrder = project.getProjectFiles().stream()
                    .mapToInt(ProjectFile::getDisplayOrder)
                    .max()
                    .orElse(-1);
            displayOrder = maxOrder + 1;
        }
        projectFile.setDisplayOrder(displayOrder);

        ProjectFile saved = projectFileRepository.save(projectFile);
        project.getProjectFiles().add(saved);
        return saved;
    }

    public List<Project> getProjectsByLanguageId(Long languageId) {
        return projectRepository.findByLanguages_Id(languageId);
    }

    public Page<Project> getTopProjectsByLanguage(Long languageId, Pageable pageable) {
        log.info("Fetching top projects by language ID: {} with pageable: {}", languageId, pageable);

        return projectRepository.findTopByLanguage(languageId,pageable);
    }
}
