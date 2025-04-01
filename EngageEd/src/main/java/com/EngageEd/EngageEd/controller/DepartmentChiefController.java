package com.EngageEd.EngageEd.controller;

import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Folder;
import com.EngageEd.EngageEd.service.DepartmentChiefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department-chiefs")
public class DepartmentChiefController {

    private final DepartmentChiefService departmentChiefService;

    @Autowired
    public DepartmentChiefController(DepartmentChiefService departmentChiefService) {
        this.departmentChiefService = departmentChiefService;
    }

    @GetMapping("/{chiefId}")
    public ResponseEntity<DepartmentChief> getDepartmentChief(@PathVariable Long chiefId) {
        DepartmentChief chief = departmentChiefService.getDepartmentChiefById(chiefId);
        return ResponseEntity.ok(chief);
    }

    @GetMapping("/{chiefId}/folders")
    public ResponseEntity<List<Folder>> getFoldersCreatedBy(@PathVariable Long chiefId) {
        List<Folder> folders = departmentChiefService.getFoldersCreatedBy(chiefId);
        return ResponseEntity.ok(folders);
    }

    @DeleteMapping("/{chiefId}/folders/{folderId}")
    public ResponseEntity<Void> deleteFolder(
            @PathVariable Long chiefId,
            @PathVariable Long folderId) {
        departmentChiefService.deleteFolder(chiefId, folderId);
        return ResponseEntity.noContent().build();
    }
}