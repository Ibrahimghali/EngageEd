package com.EngageEd.EngageEd.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EngageEd.EngageEd.entity.Folder;
import com.EngageEd.EngageEd.serviceImpl.FolderService;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;

    // @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/subject/{subjectId}")
    public List<Folder> getFoldersBySubject(@PathVariable Long subjectId) {
        return folderService.getFoldersBySubject(subjectId);
    }

    @PostMapping
    public Folder createFolder(@RequestBody Folder folder) {
        return folderService.createFolder(folder);
    }
}
