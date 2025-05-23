package com.EngageEd.EngageEd.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EngageEd.EngageEd.dto.ApiResponse;
import com.EngageEd.EngageEd.dto.MaterialDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.SubjectDTOs;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.service.MaterialService;
import com.EngageEd.EngageEd.service.ProfessorService;
import com.EngageEd.EngageEd.service.SubjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@Slf4j
public class SubjectController {

    private final SubjectService subjectService;
    private final ProfessorService professorService;
    private final MaterialService materialService; // Add this line
    
    @PostMapping
    public ResponseEntity<ApiResponse<SubjectDTOs.SubjectResponse>> createSubject(
            @RequestBody SubjectDTOs.SubjectCreationRequest request,
            Authentication authentication) {
        
        // Get the authenticated user
        String email = authentication.getName();
        Professor professor = professorService.findProfessorEntityByEmail(email);
        
        // Let the service handle the conversion
        SubjectDTOs.SubjectResponse response = subjectService.createAndReturnSubject(request, professor);
        
        return ResponseEntity.ok(ApiResponse.success("Subject created successfully", response));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectDTOs.SubjectResponse>> getSubject(
            @PathVariable UUID id) {
        log.info("Get subject request received for ID: {}", id);
        
        SubjectDTOs.SubjectResponse response = subjectService.getSubjectById(id);
        
        return ResponseEntity.ok(ApiResponse.success("Subject retrieved successfully", response));
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<SubjectDTOs.SubjectResponse>> getSubjectByCode(
            @PathVariable String code) {
        log.info("Get subject by code request received for code: {}", code);
        
        SubjectDTOs.SubjectResponse response = subjectService.getSubjectByCode(code);
        
        return ResponseEntity.ok(ApiResponse.success("Subject retrieved successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectDTOs.SubjectResponse>>> getAllSubjects() {
        log.info("Get all subjects request received");
        
        List<SubjectDTOs.SubjectResponse> response = subjectService.getAllSubjects();
        
        return ResponseEntity.ok(ApiResponse.success("Subjects retrieved successfully", response));
    }
    
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<SubjectDTOs.SubjectResponse>>> getActiveSubjects() {
        log.info("Get active subjects request received");
        
        List<SubjectDTOs.SubjectResponse> response = subjectService.getActiveSubjects();
        
        return ResponseEntity.ok(ApiResponse.success("Active subjects retrieved successfully", response));
    }
    
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<ApiResponse<List<SubjectDTOs.SubjectResponse>>> getSubjectsByProfessor(
            @PathVariable UUID professorId) {
        log.info("Get subjects by professor request received for ID: {}", professorId);
        
        List<SubjectDTOs.SubjectResponse> response = subjectService.getSubjectsByProfessor(professorId);
        
        return ResponseEntity.ok(ApiResponse.success("Subjects retrieved successfully", response));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SubjectDTOs.SubjectResponse>>> searchSubjects(
            @RequestParam String query) {
        log.info("Search subjects request received with query: {}", query);
        
        List<SubjectDTOs.SubjectResponse> response = subjectService.searchSubjects(query);
        
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", response));
    }
    
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<PageResponse<SubjectDTOs.SubjectResponse>>> getPagedSubjects(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get paged subjects request received, page: {}, size: {}", page, size);
        
        PageResponse<SubjectDTOs.SubjectResponse> response = 
                subjectService.getSubjectsPaged(page, size);
        
        return ResponseEntity.ok(ApiResponse.success("Paged subjects retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectDTOs.SubjectResponse>> updateSubject(
            @PathVariable UUID id,
            @Valid @RequestBody SubjectDTOs.SubjectUpdateRequest request,
            Authentication authentication) {  // Changed from @RequestParam UUID professorId
        
        log.info("Update subject request received for ID: {}", id);
        
        // Get the authenticated user
        String email = authentication.getName();
        Professor professor = professorService.findProfessorEntityByEmail(email);
        
        // Update the subject
        SubjectDTOs.SubjectResponse response = subjectService.updateSubject(id, request, professor);
        
        return ResponseEntity.ok(ApiResponse.success("Subject updated successfully", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(
            @PathVariable UUID id,
            Authentication authentication) {  // Changed from @RequestParam UUID professorId
        
        log.info("Delete subject request received for ID: {}", id);
        
        // Get the authenticated user
        String email = authentication.getName();
        Professor professor = professorService.findProfessorEntityByEmail(email);
        
        subjectService.deleteSubject(id, professor);
        
        return ResponseEntity.ok(ApiResponse.success("Subject deleted successfully"));
    }
    
    @GetMapping("/generate-code")
    public ResponseEntity<ApiResponse<String>> generateSubjectCode() {
        log.info("Generate subject code request received");
        
        String subjectCode = subjectService.generateUniqueSubjectCode();
        
        return ResponseEntity.ok(ApiResponse.success("Subject code generated successfully", subjectCode));
    }
    
    @PostMapping("/{id}/materials")
    public ResponseEntity<ApiResponse<MaterialDTOs.MaterialResponse>> addMaterialToSubject(
            @PathVariable UUID id,
            @Valid @RequestBody MaterialDTOs.MaterialCreationRequest request,
            Authentication authentication) {
        
        log.info("Add material to subject request received for subject ID: {}", id);
        
        // Get the authenticated user
        String email = authentication.getName();
        Professor professor = professorService.findProfessorEntityByEmail(email);
        
        // Create material
        MaterialDTOs.MaterialResponse response = materialService.createMaterial(request, id, professor);
        
        return ResponseEntity.ok(ApiResponse.success("Material added successfully", response));
    }
    
    @GetMapping("/{id}/materials")
    public ResponseEntity<ApiResponse<List<MaterialDTOs.MaterialResponse>>> getMaterialsForSubject(
            @PathVariable UUID id) {
        
        log.info("Get materials for subject request received for subject ID: {}", id);
        
        List<MaterialDTOs.MaterialResponse> materials = materialService.getMaterialsBySubject(id);
        
        return ResponseEntity.ok(ApiResponse.success("Materials retrieved successfully", materials));
    }
}