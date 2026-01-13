package com.example.projecthub.model;

public enum FileType {
    SOURCE_CODE,      // .zip, .rar containing source code
    EXECUTABLE,       // .exe, .jar, .apk
    DOCUMENTATION,    // .pdf, .docx
    DATABASE,         // .sql, .db files
    CONFIGURATION,    // .env.example, config files
    MEDIA,           // images, videos for the project
    OTHER
}
