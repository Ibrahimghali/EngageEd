package com.EngageEd.EngageEd.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.EngageEd.EngageEd.model.Document;

public interface DocumentService {
    Document uploadDocument(String title, String description, String type, MultipartFile file, Long professorId, Long folderId);
    Document renameDocument(Long documentId, String newTitle);
    byte[] downloadDocument(Long documentId);
    void deleteDocument(Long documentId);
    List<Document> getAllDocumentsByFolder(Long folderId);
}