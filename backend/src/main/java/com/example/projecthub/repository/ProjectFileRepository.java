package com.example.projecthub.repository;

import com.example.projecthub.model.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {
    // Efficient max display order query
    @Query("SELECT MAX(pf.displayOrder) FROM ProjectFile pf WHERE pf.project.id = :projectId AND pf.isActive = true")
    Optional<Integer> findMaxDisplayOrderByProjectId(@Param("projectId") Long projectId);

    List<ProjectFile> findAllByProjectId(Long projectId);
}
