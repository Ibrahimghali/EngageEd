package com.EngageEd.EngageEd.controller;

import com.EngageEd.EngageEd.model.Document;
import com.EngageEd.EngageEd.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file,
            @RequestParam("professorId") Long professorId,
            @RequestParam("folderId") Long folderId) {
        Document document = documentService.uploadDocument(title, description, type, file, professorId, folderId);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<Document>> getDocumentsByFolder(@PathVariable Long folderId) {
        List<Document> documents = documentService.getAllDocumentsByFolder(folderId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long documentId) {
        byte[] fileContent = documentService.downloadDocument(documentId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"document\"")
                .body(fileContent);
    }

    @PutMapping("/rename/{documentId}")
    public ResponseEntity<Document> renameDocument(
            @PathVariable Long documentId,
            @RequestParam String newTitle) {
        Document document = documentService.renameDocument(documentId, newTitle);
        return ResponseEntity.ok(document);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}