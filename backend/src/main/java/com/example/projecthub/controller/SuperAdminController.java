package com.example.projecthub.controller;

import com.example.projecthub.dto.user.UpdateUserStatusRequest;
import com.example.projecthub.service.admin.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
public class SuperAdminController {
    private final AdminService adminService;

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


}
