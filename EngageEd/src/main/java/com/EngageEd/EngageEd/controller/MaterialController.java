package com.EngageEd.EngageEd.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.EngageEd.EngageEd.dto.ApiResponse;
import com.EngageEd.EngageEd.dto.MaterialDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.model.MaterialType;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.service.MaterialService;
import com.EngageEd.EngageEd.service.ProfessorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@Slf4j
public class MaterialController {

    private final MaterialService materialService;
    private final ProfessorService professorService;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MaterialDTOs.MaterialResponse>> uploadMaterial(
            @Valid @RequestPart("materialData") MaterialDTOs.MaterialUploadRequest request,
            @RequestPart("file") MultipartFile file,
            @RequestParam UUID professorId) {
        log.info("Upload material request received: {} for subject ID: {} by professor ID: {}", 
                request.getTitle(), request.getSubjectId(), professorId);
        
        Professor professor = professorService.getProfessorEntityById(professorId);
        MaterialDTOs.MaterialResponse response = materialService.uploadMaterial(request, file, professor);
        
        return new ResponseEntity<>(ApiResponse.success("Material uploaded successfully", response), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MaterialDTOs.MaterialResponse>> getMaterial(
            @PathVariable UUID id) {
        log.info("Get material request received for ID: {}", id);
        
        MaterialDTOs.MaterialResponse response = materialService.getMaterialById(id);
        
        return ResponseEntity.ok(ApiResponse.success("Material retrieved successfully", response));
    }
    
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<MaterialDTOs.MaterialResponse>>> getMaterialsBySubject(
            @PathVariable UUID subjectId) {
        log.info("Get materials by subject request received for ID: {}", subjectId);
        
        List<MaterialDTOs.MaterialResponse> response = materialService.getMaterialsBySubject(subjectId);
        
        return ResponseEntity.ok(ApiResponse.success("Materials retrieved successfully", response));
    }
    
    @GetMapping("/subject/{subjectId}/type/{type}")
    public ResponseEntity<ApiResponse<List<MaterialDTOs.MaterialResponse>>> getMaterialsBySubjectAndType(
            @PathVariable UUID subjectId,
            @PathVariable MaterialType type) {
        log.info("Get materials by subject and type request received for subject ID: {} and type: {}", 
                subjectId, type);
        
        List<MaterialDTOs.MaterialResponse> response = materialService.getMaterialsBySubjectAndType(subjectId, type);
        
        return ResponseEntity.ok(ApiResponse.success("Materials retrieved successfully", response));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MaterialDTOs.MaterialResponse>>> searchMaterialsInSubject(
            @RequestParam UUID subjectId,
            @RequestParam String query) {
        log.info("Search materials request received for subject ID: {} with query: {}", 
                subjectId, query);
        
        List<MaterialDTOs.MaterialResponse> response = materialService.searchMaterialsInSubject(subjectId, query);
        
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", response));
    }
    
    @GetMapping("/subject/{subjectId}/paged")
    public ResponseEntity<ApiResponse<PageResponse<MaterialDTOs.MaterialResponse>>> getPagedMaterialsBySubject(
            @PathVariable UUID subjectId,
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get paged materials request received for subject ID: {}, page: {}, size: {}", 
                subjectId, page, size);
        
        PageResponse<MaterialDTOs.MaterialResponse> response = 
                materialService.getMaterialsBySubjectPaged(subjectId, page, size);
        
        return ResponseEntity.ok(ApiResponse.success("Paged materials retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MaterialDTOs.MaterialResponse>> updateMaterial(
            @PathVariable UUID id,
            @Valid @RequestPart("materialData") MaterialDTOs.MaterialUpdateRequest request,
            @RequestParam UUID professorId) {
        log.info("Update material request received for ID: {} by professor ID: {}", id, professorId);
        
        Professor professor = professorService.getProfessorEntityById(professorId);
        MaterialDTOs.MaterialResponse response = materialService.updateMaterial(id, request, professor);
        
        return ResponseEntity.ok(ApiResponse.success("Material updated successfully", response));
    }
    
    @PutMapping(value = "/{id}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MaterialDTOs.MaterialResponse>> replaceMaterialFile(
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file,
            @RequestParam UUID professorId) {
        log.info("Replace material file request received for ID: {} by professor ID: {}", id, professorId);
        
        Professor professor = professorService.getProfessorEntityById(professorId);
        MaterialDTOs.MaterialResponse response = materialService.replaceMaterialFile(id, file, professor);
        
        return ResponseEntity.ok(ApiResponse.success("Material file replaced successfully", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMaterial(
            @PathVariable UUID id,
            @RequestParam UUID professorId) {
        log.info("Delete material request received for ID: {} by professor ID: {}", id, professorId);
        
        Professor professor = professorService.getProfessorEntityById(professorId);
        materialService.deleteMaterial(id, professor);
        
        return ResponseEntity.ok(ApiResponse.success("Material deleted successfully"));
    }
}