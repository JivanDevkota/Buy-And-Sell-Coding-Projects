package com.example.projecthub.controller;

import com.example.projecthub.dto.project.ProjectResponse;
import com.example.projecthub.dto.user.UpdateUserStatusRequest;
import com.example.projecthub.service.admin.AdminService;
import com.example.projecthub.service.project.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
public class SuperAdminController {
    private final AdminService adminService;
    private final ProjectService projectService;

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

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", java.time.LocalDateTime.now().toString());
        return error;
    }
}
