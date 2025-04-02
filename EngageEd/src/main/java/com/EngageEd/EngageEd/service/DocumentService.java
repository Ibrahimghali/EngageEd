package com.EngageEd.EngageEd.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.EngageEd.EngageEd.dto.document.DocumentRequestDTO;
import com.EngageEd.EngageEd.dto.document.DocumentResponseDTO;
import com.EngageEd.EngageEd.model.Document;

public interface DocumentService {
    // New methods using DTOs
    DocumentResponseDTO uploadDocument(DocumentRequestDTO dto, MultipartFile file, Long professorId);
    DocumentResponseDTO renameDocument(Long documentId, String newTitle);
    DocumentResponseDTO findById(Long documentId);
    List<DocumentResponseDTO> getAllDocumentsByFolder(Long folderId);
    
    // Legacy methods for backward compatibility
    Document uploadDocument(String title, String description, String type, 
                          MultipartFile file, Long professorId, Long folderId);
    
    // Methods that don't need DTO conversion (binary data, void)
    byte[] downloadDocument(Long documentId);
    void deleteDocument(Long documentId);
}