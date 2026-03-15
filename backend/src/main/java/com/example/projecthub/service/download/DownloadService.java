package com.example.projecthub.service.download;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

public interface DownloadService {
    StreamingResponseBody downloadAllProjectFiles(Long projectId, Long userId, HttpServletResponse response) throws IOException;
    Resource downloadProjectFile(Long fileId, Long userId) throws IOException;
}
