package com.EngageEd.EngageEd.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    
    String storeFile(MultipartFile file, String prefix) throws IOException;
    
    void deleteFile(String fileUrl) throws IOException;
}
