package com.example.projecthub.service.admin;

import com.example.projecthub.dto.user.UserDetailsResponse;
import com.example.projecthub.model.Status;
import com.example.projecthub.model.User;
import com.example.projecthub.model.UserStatusHistory;
import com.example.projecthub.repository.UserRepository;
import com.example.projecthub.repository.UserStatusHistoryRepository;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserStatusHistoryRepository userStatusHistoryRepository;

    @Override
    public void updateUserStatus(
            Long userId,
            Status newStatus,
            String reason,
            String changedBy
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Status currentStatus = user.getStatus();

        // 🔒 VALIDATE TRANSITION (clean & professional)
        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    "Invalid status transition: " + currentStatus + " → " + newStatus
            );
        }

        // 📝 SAVE HISTORY
        UserStatusHistory history = new UserStatusHistory();
        history.setUser(user);
        history.setOldStatus(currentStatus);
        history.setNewStatus(newStatus);
        history.setReason(reason);
        history.setChangedBy(changedBy);
        history.setChangedAt(LocalDateTime.now());

        userStatusHistoryRepository.save(history);

        // ✅ UPDATE USER STATUS (YOU MISSED THIS EARLIER)
        user.setStatus(newStatus);
        userRepository.save(user);
    }

    public Map<String, Object> getRecentlyJoinedUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = userRepository.findAllByOrderByCreatedAtDesc(pageable);

        // FIX: Changed from Positive.List to List and fixed toDto() call
        List<UserDetailsResponse> users = userPage.getContent().stream()
                .map(UserDetailsResponse::toDto)  // Changed from toDto() to ::toDto
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("currentPage", userPage.getNumber());
        response.put("totalPages", userPage.getTotalPages());
        response.put("totalItems", userPage.getTotalElements());
        response.put("hasNext", userPage.hasNext());
        response.put("hasPrevious", userPage.hasPrevious());
        return response;
    }
}
