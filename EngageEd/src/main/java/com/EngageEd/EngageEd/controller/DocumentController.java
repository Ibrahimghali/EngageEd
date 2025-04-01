package com.EngageEd.EngageEd.controller;

import java.util.List;

import com.EngageEd.EngageEd.model.Document;
import com.EngageEd.EngageEd.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String type,
            @RequestParam MultipartFile file,
            @RequestParam Long professorId,
            @RequestParam Long folderId) {
        
        Document document = documentService.uploadDocument(title, description, type, file, professorId, folderId);
        return ResponseEntity.ok(document);
    }

    @PutMapping("/{documentId}/rename")
    public ResponseEntity<Document> renameDocument(@PathVariable Long documentId, @RequestParam String newTitle) {
        Document document = documentService.renameDocument(documentId, newTitle);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long documentId) {
        byte[] fileContent = documentService.downloadDocument(documentId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document_" + documentId);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.ok("Document deleted successfully.");
    }

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<Document>> getAllDocumentsByFolder(@PathVariable Long folderId) {
        List<Document> documents = documentService.getAllDocumentsByFolder(folderId);
        return ResponseEntity.ok(documents);
    }
}