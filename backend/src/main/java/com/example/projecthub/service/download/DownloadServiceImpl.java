package com.example.projecthub.service.download;

import com.example.projecthub.model.ProjectFile;
import com.example.projecthub.repository.ProjectFileRepository;
import com.example.projecthub.repository.PurchaseRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DownloadServiceImpl implements DownloadService {

    private final ProjectFileRepository projectFileRepository;
    private final PurchaseRepository purchaseRepository;


    @Value("${filePath}")          // ✅ declared here — used in both methods
    private String filePath;

    // --- single file download ---
    @Transactional
    public Resource downloadProjectFile(Long fileId, Long userId) throws IOException {
        ProjectFile projectFile = projectFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + fileId));

        boolean hasPurchased = purchaseRepository.existsByBuyerIdAndProjectId(userId, projectFile.getProject().getId());
        if (!hasPurchased) throw new RuntimeException("Access denied.");

        String filename = projectFile.getFileUrl().replace("/files/", "");
        Path resolvedPath = Paths.get(filePath).resolve(filename).normalize();  // ✅ filePath used here

        if (!resolvedPath.startsWith(Paths.get(filePath).normalize()))
            throw new RuntimeException("Invalid file path.");

        Resource resource = new UrlResource(resolvedPath.toUri());
        if (!resource.exists() || !resource.isReadable())
            throw new RuntimeException("File not found on server.");

        projectFile.setDownloadCount(
                projectFile.getDownloadCount() == null ? 1 : projectFile.getDownloadCount() + 1
        );
        projectFileRepository.save(projectFile);
        return resource;
    }

    // --- download all as ZIP ---
    @Transactional
    public StreamingResponseBody downloadAllProjectFiles(Long projectId, Long userId, HttpServletResponse response) throws IOException {

        boolean hasPurchased = purchaseRepository.existsByBuyerIdAndProjectId(userId, projectId);
        if (!hasPurchased) throw new RuntimeException("Access denied. You have not purchased this project.");

        List<ProjectFile> projectFiles = projectFileRepository.findAllByProjectId(projectId);
        if (projectFiles.isEmpty()) throw new RuntimeException("No files found for project ID: " + projectId);

        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"project-" + projectId + ".zip\"");

        return outputStream -> {
            try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(outputStream))) {
                for (ProjectFile projectFile : projectFiles) {
                    String filename = projectFile.getFileUrl().replace("/files/", "");
                    Path resolvedPath = Paths.get(filePath).resolve(filename).normalize();  // ✅ filePath used here

                    if (!resolvedPath.startsWith(Paths.get(filePath).normalize())) {
                        log.warn("Skipping suspicious path: {}", filename);
                        continue;
                    }

                    Resource resource = new UrlResource(resolvedPath.toUri());
                    if (!resource.exists() || !resource.isReadable()) {
                        log.warn("Skipping missing file: {}", filename);
                        continue;
                    }

                    String entryName = projectFile.getFileName() != null ? projectFile.getFileName() : filename;
                    zipOut.putNextEntry(new ZipEntry(entryName));
                    StreamUtils.copy(resource.getInputStream(), zipOut);
                    zipOut.closeEntry();

                    projectFile.setDownloadCount(
                            projectFile.getDownloadCount() == null ? 1 : projectFile.getDownloadCount() + 1
                    );
                    projectFileRepository.save(projectFile);
                    log.info("Added to ZIP: {} for project {}", entryName, projectId);
                }
            }
        };
    }
}
