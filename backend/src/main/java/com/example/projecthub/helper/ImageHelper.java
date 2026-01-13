package com.example.projecthub.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class ImageHelper {

    @Value("${imgPath}")
    private String imgPath;

    public String uploadImage(MultipartFile file) throws IOException {
        System.out.println("ImagePath value: "+imgPath);
        if (file.isEmpty() || file == null) {
            throw new RuntimeException("File is empty");
        }
        Path path = Paths.get(imgPath);
        System.out.println("Absolute path: " + path.toAbsolutePath()); // Debug line
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("Created directory: " + path); // Debug line
        }
        //get orginal filename
        String fileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(fileName);
        String random = UUID.randomUUID().toString();
        String uniqueFileName = random + fileExtension;

        Path targetPath = path.resolve(uniqueFileName);   // upload/+img/123.png
        System.out.println("Saving to: " + targetPath.toAbsolutePath()); // Debug line
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//        file.transferTo(targetPath.toFile());
        return "/img/" + uniqueFileName;
    }

    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
