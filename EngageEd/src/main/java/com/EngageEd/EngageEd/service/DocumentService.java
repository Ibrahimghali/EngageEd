package com.EngageEd.EngageEd.service;

import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.dto.Document;
import com.EngageEd.EngageEd.repository.DocumentRepo;

@Service
public class DocumentService {
    private final DocumentRepo documentRepository;

    // @Autowired
    public DocumentService(DocumentRepo documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> getDocumentsByFolder(Long folderId) {
        return documentRepository.findByFolderId(folderId);
    }

    public Document uploadDocument(Document document) {
        return documentRepository.save(document);
    }
}
