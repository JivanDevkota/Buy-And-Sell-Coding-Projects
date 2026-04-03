package com.example.projecthub.controller;

import com.example.projecthub.dto.buyer.BuyerSummaryDTO;
import com.example.projecthub.dto.dashboard.DashboardAdminStats;
import com.example.projecthub.dto.language.LanguageDTO;
import com.example.projecthub.dto.project.PendingProjects;
import com.example.projecthub.dto.project.ProjectResponse;
import com.example.projecthub.dto.seller.PagedResponse;
import com.example.projecthub.dto.seller.PendingSellerApprovals;
import com.example.projecthub.dto.seller.SellerSummaryDTO;
import com.example.projecthub.dto.user.UpdateUserStatusRequest;
import com.example.projecthub.model.Status;
import com.example.projecthub.service.admin.AdminService;
import com.example.projecthub.service.language.LanguageService;
import com.example.projecthub.service.project.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
public class SuperAdminController {
    private final AdminService adminService;
    private final ProjectService projectService;

    private final LanguageService languageService;

    @PostMapping("/create/language")
    public ResponseEntity<LanguageDTO>createProgrammingLanguage(@RequestPart("language") LanguageDTO languageDTO, @RequestPart("file") MultipartFile file)throws IOException {
        if (file.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty file");
        }
        LanguageDTO programmingLanguage = languageService.createProgrammingLanguage(languageDTO, file);
        return ResponseEntity.ok().body(programmingLanguage);
    }

    @GetMapping("/languages")
    public ResponseEntity<List<LanguageDTO>> getAllLanguages() {
        List<LanguageDTO> languages = languageService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }
    /**
     * Update user status with reason (Admin only)
     */
    @PatchMapping("/{userId}/status")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUserStatusRequest request,
            Principal principal
    ) {

        log.info("updateUserStatus");
        adminService.updateUserStatus(
                userId,
                request.getStatus(),
                request.getReason(),
                principal.getName()   // admin username/email
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/recent/users")
    public ResponseEntity<Map<String, Object>> getRecentlyJoinedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response =
                adminService.getRecentlyJoinedUsersPaginated(page, size);

        return ResponseEntity.ok(response);
    }


    /**
     * Approve a project
     */
    @PutMapping("/project/{projectId}/approve")
    public ResponseEntity<?> approveProject(@PathVariable Long projectId) {
        try {
            log.info("Admin approving project ID: {}", projectId);
            ProjectResponse project = projectService.approveProject(projectId);
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            log.error("Error approving project: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Suspend a project
     */
    @PutMapping("/project/{projectId}/suspend")
    public ResponseEntity<?> suspendProject(@PathVariable Long projectId) {
        try {
            log.info("Admin suspending project ID: {}", projectId);
            ProjectResponse project = projectService.suspendProject(projectId);
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            log.error("Error suspending project: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

//    @GetMapping("/pending-projects")
//    public ResponseEntity<Page<PendingProjects>> getPendingProjects(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size
//    ) {
//        return ResponseEntity.ok(
//                projectService.getUnderReviewProjects(page, size)
//        );
//    }

    @GetMapping("/pending-projects")
    public ResponseEntity<Map<String, Object>> getPendingProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<PendingProjects> projects = projectService.getUnderReviewProjects(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", projects.getContent());
        response.put("page", projects.getNumber());
        response.put("size", projects.getSize());
        response.put("totalElements", projects.getTotalElements());
        response.put("totalPages", projects.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/stats")
    public ResponseEntity<DashboardAdminStats> getUserDashboardStats() {
        DashboardAdminStats dashboardStats = adminService.getDashboardStats();
        return ResponseEntity.ok(dashboardStats);
    }

    @GetMapping("/sellers")
    public ResponseEntity<PagedResponse<SellerSummaryDTO>> getSellers(
            @RequestParam(required = false) Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.getSellers(status, pageable));
    }

    @GetMapping("/top-buyers")
    public ResponseEntity<List<BuyerSummaryDTO>> getTopBuyers() {
        return ResponseEntity.ok(adminService.getTop5Buyers());
    }

    @GetMapping("/pending-approvals")
    private ResponseEntity<Map<String, Long>> getPendingApprovals() {
        Map<String, Long> pendingApprovals = new HashMap<>();
        pendingApprovals.put("PENDING SELLERS", adminService.countSellerByStatus());
        pendingApprovals.put("PENDING PROJECTS", adminService.countProjectByStatus());
        return ResponseEntity.ok(pendingApprovals);
    }

    @GetMapping("/pending-sellers-approvals")
    private ResponseEntity<List<PendingSellerApprovals>>getPendingSellersApprovals(){
        List<PendingSellerApprovals> allSellerPendingApprovals = adminService.getAllSellerPendingApprovals();
        return ResponseEntity.ok(allSellerPendingApprovals);
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", java.time.LocalDateTime.now().toString());
        return error;
    }
}
