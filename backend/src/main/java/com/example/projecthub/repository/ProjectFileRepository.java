package com.example.projecthub.repository;

import com.example.projecthub.model.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {
}
