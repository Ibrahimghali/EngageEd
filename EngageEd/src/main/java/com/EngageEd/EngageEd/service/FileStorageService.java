package com.EngageEd.EngageEd.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for file storage operations
 */
public interface FileStorageService {
    
    /**
     * Store a file and return its URL
     * 
     * @param file The file to store
     * @param prefix A prefix for organization (e.g., subject code)
     * @return The URL to access the file
     * @throws IOException if there's an error storing the file
     */
    String storeFile(MultipartFile file, String prefix) throws IOException;
    
    /**
     * Delete a file by its URL
     * 
     * @param fileUrl The URL of the file to delete
     * @throws IOException if there's an error deleting the file
     */
    void deleteFile(String fileUrl) throws IOException;
}