package com.EngageEd.EngageEd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.EngageEd.EngageEd.dto.folder.FolderRequestDTO;
import com.EngageEd.EngageEd.dto.folder.FolderResponseDTO;
import com.EngageEd.EngageEd.service.FolderService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/folders")
@Validated
public class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    /**
     * Create a new folder
     */
    @PostMapping
    public ResponseEntity<FolderResponseDTO> createFolder(
            @Valid @RequestBody FolderRequestDTO requestDTO,
            @RequestParam("departmentChiefId") Long departmentChiefId) {
        
        FolderResponseDTO created = folderService.createFolder(requestDTO, departmentChiefId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Get a folder by ID
     */
    @GetMapping("/{folderId}")
    public ResponseEntity<FolderResponseDTO> getFolder(@PathVariable Long folderId) {
        FolderResponseDTO folder = folderService.getFolderById(folderId);
        return ResponseEntity.ok(folder);
    }

    /**
     * Get all folders
     */
    @GetMapping
    public ResponseEntity<List<FolderResponseDTO>> getAllFolders() {
        List<FolderResponseDTO> folders = folderService.getAllFolders();
        return ResponseEntity.ok(folders);
    }

    /**
     * Rename a folder
     */
    @PutMapping("/{folderId}/rename")
    public ResponseEntity<FolderResponseDTO> renameFolder(
            @PathVariable Long folderId,
            @RequestParam("newName") String newName) {
        
        FolderResponseDTO updated = folderService.renameFolder(folderId, newName);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a folder
     */
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Exception handler for folder operations
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}