package com.example.projecthub.repository;

import com.example.projecthub.model.Download;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DownloadRepository extends JpaRepository<Download, Long> {
}
