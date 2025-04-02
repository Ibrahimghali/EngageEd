package com.EngageEd.EngageEd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.EngageEd.EngageEd.dto.document.DocumentRequestDTO;
import com.EngageEd.EngageEd.dto.document.DocumentResponseDTO;
import com.EngageEd.EngageEd.service.DocumentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/documents")
@Validated
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Upload a new document
     */
    @PostMapping
    public ResponseEntity<DocumentResponseDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute @Valid DocumentRequestDTO dto,
            @RequestParam("professorId") Long professorId) {
        
        DocumentResponseDTO response = documentService.uploadDocument(dto, file, professorId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get document details by ID
     */
    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponseDTO> getDocument(@PathVariable Long documentId) {
        DocumentResponseDTO document = documentService.findById(documentId);
        return ResponseEntity.ok(document);
    }

    /**
     * Download document file
     */
    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
        DocumentResponseDTO document = documentService.findById(documentId);
        byte[] fileContent = documentService.downloadDocument(documentId);
        
        ByteArrayResource resource = new ByteArrayResource(fileContent);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getTitle());
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileContent.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    /**
     * Rename a document
     */
    @PutMapping("/{documentId}/rename")
    public ResponseEntity<DocumentResponseDTO> renameDocument(
            @PathVariable Long documentId,
            @RequestParam("newTitle") String newTitle) {
        
        DocumentResponseDTO updatedDocument = documentService.renameDocument(documentId, newTitle);
        return ResponseEntity.ok(updatedDocument);
    }

    /**
     * Delete a document
     */
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all documents in a folder
     */
    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsByFolder(@PathVariable Long folderId) {
        List<DocumentResponseDTO> documents = documentService.getAllDocumentsByFolder(folderId);
        return ResponseEntity.ok(documents);
    }

    /**
     * Exception handler for document operations
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}