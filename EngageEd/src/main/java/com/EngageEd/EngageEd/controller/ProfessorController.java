package com.EngageEd.EngageEd.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
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
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.ProfessorDTOs;
import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.service.DepartmentChiefService;
import com.EngageEd.EngageEd.service.ProfessorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/professors")
@RequiredArgsConstructor
@Slf4j
public class ProfessorController {

    private final ProfessorService professorService;
    private final DepartmentChiefService departmentChiefService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<ProfessorDTOs.ProfessorResponse>> registerProfessor(
            @Valid @RequestBody ProfessorDTOs.ProfessorRegistrationRequest request) {
        log.info("Register professor request received: {}", request.getEmail());
        
        ProfessorDTOs.ProfessorResponse response = professorService.registerProfessor(request);
        
        return new ResponseEntity<>(ApiResponse.success("Professor registered successfully", response), HttpStatus.CREATED);
    }
    
    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<ProfessorDTOs.ProfessorResponse>> inviteProfessor(
            @Valid @RequestBody ProfessorDTOs.ProfessorInviteRequest request,
            Authentication authentication) {
        log.info("Invite professor request received: {}", request.getEmail());
        
        // Get authenticated department chief
        String email = authentication.getName();
        DepartmentChief departmentChief = departmentChiefService.findDepartmentChiefEntityByEmail(email);
        
        ProfessorDTOs.ProfessorResponse response = professorService.inviteProfessor(request, departmentChief);
        
        return new ResponseEntity<>(ApiResponse.success("Professor invited successfully", response), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfessorDTOs.ProfessorResponse>> getProfessor(
            @PathVariable UUID id) {
        log.info("Get professor request received for ID: {}", id);
        
        ProfessorDTOs.ProfessorResponse response = professorService.getProfessorById(id);
        
        return ResponseEntity.ok(ApiResponse.success("Professor retrieved successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProfessorDTOs.ProfessorResponse>>> getAllProfessors() {
        log.info("Get all professors request received");
        
        List<ProfessorDTOs.ProfessorResponse> response = professorService.getAllProfessors();
        
        return ResponseEntity.ok(ApiResponse.success("Professors retrieved successfully", response));
    }
    
    @GetMapping("/department-chief/{departmentChiefId}")
    public ResponseEntity<ApiResponse<List<ProfessorDTOs.ProfessorResponse>>> getProfessorsByDepartmentChief(
            @PathVariable UUID departmentChiefId) {
        log.info("Get professors by department chief request received for ID: {}", departmentChiefId);
        
        List<ProfessorDTOs.ProfessorResponse> response = professorService.getProfessorsByDepartmentChief(departmentChiefId);
        
        return ResponseEntity.ok(ApiResponse.success("Professors retrieved successfully", response));
    }
    
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<PageResponse<ProfessorDTOs.ProfessorResponse>>> getPagedProfessors(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get paged professors request received, page: {}, size: {}", page, size);
        
        PageResponse<ProfessorDTOs.ProfessorResponse> response = 
                professorService.getProfessorsPaged(page, size);
        
        return ResponseEntity.ok(ApiResponse.success("Paged professors retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfessorDTOs.ProfessorResponse>> updateProfessor(
            @PathVariable UUID id,
            @Valid @RequestBody ProfessorDTOs.ProfessorUpdateRequest request) {
        log.info("Update professor request received for ID: {}", id);
        
        ProfessorDTOs.ProfessorResponse response = professorService.updateProfessor(id, request);
        
        return ResponseEntity.ok(ApiResponse.success("Professor updated successfully", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProfessor(
            @PathVariable UUID id) {
        log.info("Delete professor request received for ID: {}", id);
        
        professorService.deleteProfessor(id);
        
        return ResponseEntity.ok(ApiResponse.success("Professor deleted successfully"));
    }
}