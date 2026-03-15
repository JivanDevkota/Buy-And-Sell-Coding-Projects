package com.example.projecthub.controller;

import com.example.projecthub.dto.project.ProjectDTO;
import com.example.projecthub.dto.project.ProjectDetailsResponse;
import com.example.projecthub.dto.project.ProjectResponse;
import com.example.projecthub.dto.projectfile.ProjectFileDTO;
import com.example.projecthub.service.project.ProjectService;
import com.example.projecthub.service.purchase.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller")
public class SellerController {

    private final ProjectService projectService;
    private final PurchaseService purchaseService;

    /**
     * Creates a new project
     * @param projectDTO project data as JSON
     * @param file project image file
     * @return created project
     */
    @PostMapping(value = "/create/project", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProject(
            @RequestPart("project") @Valid ProjectDTO projectDTO,
            @RequestPart("file") MultipartFile file) {

        try {
            log.info("Received request to create project: {}", projectDTO.getTitle());

            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Project image file is required"));
            }

            ProjectDTO createdProject = projectService.createProject(projectDTO, file);
            log.info("Project created successfully with ID: {}", createdProject.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);

        } catch (IOException e) {
            log.error("Error uploading file: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to upload file: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating project: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Adds a file to an existing project
     * @param projectFileDTO file metadata as JSON
     * @param file the file to upload
     * @param userId user ID from authentication (or pass via DTO for testing)
     * @return created project file
     */
    @PostMapping(value = "/project/add-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProjectFile(
            @RequestPart("projectFile") @Valid ProjectFileDTO projectFileDTO,
            @RequestPart("file") MultipartFile file,
            @RequestParam Long userId
            ) {  // In production, get from @AuthenticationPrincipal

        try {
            log.info("Received request to add file to project ID: {}", projectFileDTO.getProjectId());

            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("File is required"));
            }

            ProjectFileDTO addedFile = projectService.addFileToProject(projectFileDTO, file, userId);
            log.info("File added successfully to project");

            return ResponseEntity.status(HttpStatus.CREATED).body(addedFile);

        } catch (IOException e) {
            log.error("Error uploading file: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to upload file: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Error adding file to project: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{userId}/projects")
    public ResponseEntity<?>getMyProjects(@PathVariable("userId") Long userId) {
        List<ProjectResponse> myProjects = projectService.getMyProjects(userId);
//        log.info("My projects: {}", myProjects);
        return ResponseEntity.status(HttpStatus.OK).body(myProjects);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getProjectDetails(@PathVariable("projectId") Long projectId) {
        try {
            ProjectDetailsResponse projectDetails = projectService.getProjectDetailsById(projectId);
            return ResponseEntity.ok(projectDetails);
        } catch (Exception e) {
            log.error("Error fetching project details: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/project/{projectId}/submit-for-review")
    public ResponseEntity<?> submitProjectForReview(@PathVariable Long projectId) {
        try {
            log.info("Admin submitting project ID: {}", projectId);
            ProjectResponse projectResponse = projectService.submitForReview(projectId);
            return ResponseEntity.ok(projectResponse);
        }catch (Exception e) {
            log.error("Error submitting project: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/project/{projectId}/withdraw")
    public ResponseEntity<?> withdrawFromReview(@PathVariable Long projectId) {
        try {
            log.info("Withdrawn project ID: {}", projectId);
            ProjectResponse projectResponse = projectService.withdrawFromReview(projectId);
            return ResponseEntity.ok(projectResponse);
        }catch (Exception e) {
            log.error("Error withdrawnFromReview project: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?>getStats(){
        return ResponseEntity.ok(Map
                .of("today", purchaseService.getTodaySales(),
                        "thisWeek",purchaseService.getWeeklySales(),
                        "thisMonth",purchaseService.getMonthlySales()
                )
        );
    }

    /**
     * Helper method to create error response
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", java.time.LocalDateTime.now().toString());
        return error;
    }
}