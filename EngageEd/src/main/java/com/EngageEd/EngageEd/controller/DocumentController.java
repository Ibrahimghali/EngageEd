package com.EngageEd.EngageEd.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EngageEd.EngageEd.entity.Document;
import com.EngageEd.EngageEd.serviceImpl.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    // @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/folder/{folderId}")
    public List<Document> getDocumentsByFolder(@PathVariable Long folderId) {
        return documentService.getDocumentsByFolder(folderId);
    }

    @PostMapping
    public Document uploadDocument(@RequestBody Document document) {
        return documentService.uploadDocument(document);
    }
}