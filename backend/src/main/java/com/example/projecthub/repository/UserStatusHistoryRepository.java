package com.example.projecthub.repository;

import com.example.projecthub.model.UserStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStatusHistoryRepository extends JpaRepository<UserStatusHistory, Long> {
    List<UserStatusHistory> findByUserIdOrderByChangedAtDesc(Long userId);
}
