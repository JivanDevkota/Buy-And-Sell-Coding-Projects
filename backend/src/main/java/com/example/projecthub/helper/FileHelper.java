package com.example.projecthub.helper;

import com.example.projecthub.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileHelper {

    @Value("${filePath}")
    private String filePath;

    @Value("${max.file.size:104857600}") // 100MB default for project files
    private long maxFileSize;

    // Allow various file types for project files
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            // Archives
            ".zip", ".rar", ".7z", ".tar", ".gz",
            // Source code
            ".java", ".py", ".js", ".ts", ".html", ".css", ".cpp", ".c", ".h",
            // Documents
            ".pdf", ".doc", ".docx", ".txt", ".md",
            // Database
            ".sql", ".db", ".sqlite",
            // Configuration
            ".json", ".xml", ".yaml", ".yml", ".env", ".properties",
            // Images (for project screenshots)
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp", ".svg",
            // Videos
            ".mp4", ".avi", ".mov", ".wmv"
            // NOTE: Removed .exe, .jar, .war, .apk for security reasons
    );

    /**
     * Uploads a file to the configured directory
     * @param file the file to upload
     * @return the relative URL path to access the file
     * @throws IOException if file upload fails
     */
    public String uploadFile(MultipartFile file) throws IOException {
        log.info("Attempting to upload file: {}", file.getOriginalFilename());

        // Validate file
        validateFile(file);

        // Ensure upload directory exists
        Path uploadPath = Paths.get(filePath);
        log.debug("Upload path: {}", uploadPath.toAbsolutePath());

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created upload directory: {}", uploadPath.toAbsolutePath());
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Save file
        Path targetPath = uploadPath.resolve(uniqueFilename);
        log.debug("Saving file to: {}", targetPath.toAbsolutePath());

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        log.info("File uploaded successfully: {}", uniqueFilename);
        return "/files/" + uniqueFilename;
    }

    /**
     * Validates the uploaded file
     * @param file the file to validate
     * @throws ValidationException if validation fails
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("File is required and cannot be empty");
        }

        // Check file size
        if (file.getSize() > maxFileSize) {
            throw new ValidationException(
                    String.format("File size exceeds maximum allowed size of %d bytes (%.2f MB)",
                            maxFileSize, maxFileSize / (1024.0 * 1024.0))
            );
        }

        // Check file extension
        String filename = file.getOriginalFilename();
        if (filename == null || filename.trim().isEmpty()) {
            throw new ValidationException("File must have a valid filename");
        }

        String extension = getFileExtension(filename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ValidationException(
                    String.format("File extension %s is not allowed. Allowed extensions: %s",
                            extension, ALLOWED_EXTENSIONS)
            );
        }
    }

    /**
     * Extracts file extension from filename
     * @param filename the filename
     * @return the file extension including the dot
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }

        return filename.substring(lastDotIndex).toLowerCase();
    }

    /**
     * Deletes a file from the upload directory
     * @param fileUrl the relative URL of the file
     * @return true if deletion was successful
     */
    public boolean deleteFile(String fileUrl) {
        try {
            if (fileUrl == null || fileUrl.isEmpty()) {
                return false;
            }

            // Extract filename from URL
            String filename = fileUrl.replace("/files/", "");
            Path filePath = Paths.get(this.filePath).resolve(filename);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("File deleted successfully: {}", filename);
                return true;
            }

            log.warn("File not found for deletion: {}", filename);
            return false;

        } catch (IOException e) {
            log.error("Error deleting file: {}", fileUrl, e);
            return false;
        }
    }
}