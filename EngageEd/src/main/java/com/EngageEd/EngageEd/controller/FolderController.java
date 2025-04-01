package com.EngageEd.EngageEd.controller;

import com.EngageEd.EngageEd.model.Folder;
import com.EngageEd.EngageEd.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
public class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Folder> createFolder(@RequestParam String name, @RequestParam Long departmentChiefId) {
        Folder folder = folderService.createFolder(name, departmentChiefId);
        return ResponseEntity.ok(folder);
    }

    @PutMapping("/{folderId}/rename")
    public ResponseEntity<Folder> renameFolder(@PathVariable Long folderId, @RequestParam String newName) {
        Folder folder = folderService.renameFolder(folderId, newName);
        return ResponseEntity.ok(folder);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Folder>> getAllFolders() {
        List<Folder> folders = folderService.getAllFolders();
        return ResponseEntity.ok(folders);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<String> deleteFolder(@PathVariable Long folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.ok("Folder deleted successfully.");
    }
}

