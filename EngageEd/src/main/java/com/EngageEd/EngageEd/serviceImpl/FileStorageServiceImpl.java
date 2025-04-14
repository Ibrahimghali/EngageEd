package com.EngageEd.EngageEd.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.EngageEd.EngageEd.exception.FileStorageException;
import com.EngageEd.EngageEd.service.FileStorageService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;
    
    @Value("${file.base-url:http://localhost:8080/api/files/}")
    private String baseUrl;
    
    private Path fileStorageLocation;
    
    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored", e);
        }
    }
    
    @Override
    public String storeFile(MultipartFile file, String prefix) throws IOException {
        // Clean the filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Check for invalid characters
        if (originalFilename.contains("..")) {
            throw new FileStorageException("Filename contains invalid path sequence: " + originalFilename);
        }
        
        // Create a unique filename with prefix
        String uniqueFilename = prefix + "_" + UUID.randomUUID().toString() + "_" + originalFilename;
        
        // Store the file
        Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        log.info("Stored file: {} at location: {}", originalFilename, targetLocation);
        
        // Return the URL
        return baseUrl + uniqueFilename;
    }
    
    @Override
    public void deleteFile(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        
        // Extract the filename from the URL
        String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        Path filePath = this.fileStorageLocation.resolve(filename);
        
        Files.deleteIfExists(filePath);
        log.info("Deleted file: {}", filePath);
    }
}