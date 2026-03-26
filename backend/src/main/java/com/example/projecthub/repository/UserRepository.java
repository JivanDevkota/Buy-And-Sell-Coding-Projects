package com.example.projecthub.repository;

import com.example.projecthub.dto.dashboard.RoleCountDTO;
import com.example.projecthub.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<User> findAllByOrderByIdAsc(Pageable pageable);

    List<User> findAllByRolesName(String rolesName, Pageable pageable);

    @Query("select new com.example.projecthub.dto.dashboard.RoleCountDTO(r.name, count(u)) " +
            "from User u join u.roles r " +
            "group by r.name")
    List<RoleCountDTO> countUsersByRole();
}