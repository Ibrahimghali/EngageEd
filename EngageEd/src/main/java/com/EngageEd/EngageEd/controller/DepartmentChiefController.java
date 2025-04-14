package com.EngageEd.EngageEd.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
import com.EngageEd.EngageEd.dto.DepartmentChiefDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.service.DepartmentChiefService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/department-chiefs")
@RequiredArgsConstructor
@Slf4j
public class DepartmentChiefController {

    private final DepartmentChiefService departmentChiefService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentChiefDTOs.DepartmentChiefResponse>> createDepartmentChief(
            @Valid @RequestBody DepartmentChiefDTOs.DepartmentChiefRegistrationRequest request,
            Authentication authentication) {  // Add authentication
        log.info("Create department chief request received: {}", request.getEmail());
        
        // Check if user has ADMIN role
        if (!hasAdminRole(authentication)) {
            throw new AccessDeniedException("Only administrators can create department chiefs");
        }
        
        DepartmentChief departmentChief = departmentChiefService.createDepartmentChief(request);
        DepartmentChiefDTOs.DepartmentChiefResponse response = departmentChiefService.getDepartmentChiefById(departmentChief.getId());
        
        return new ResponseEntity<>(ApiResponse.success("Department chief created successfully", response), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentChiefDTOs.DepartmentChiefResponse>> getDepartmentChief(
            @PathVariable UUID id) {
        log.info("Get department chief request received for ID: {}", id);
        
        DepartmentChiefDTOs.DepartmentChiefResponse response = departmentChiefService.getDepartmentChiefById(id);
        
        return ResponseEntity.ok(ApiResponse.success("Department chief retrieved successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentChiefDTOs.DepartmentChiefResponse>>> getAllDepartmentChiefs() {
        log.info("Get all department chiefs request received");
        
        List<DepartmentChiefDTOs.DepartmentChiefResponse> response = departmentChiefService.getAllDepartmentChiefs();
        
        return ResponseEntity.ok(ApiResponse.success("Department chiefs retrieved successfully", response));
    }
    
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<PageResponse<DepartmentChiefDTOs.DepartmentChiefResponse>>> getPagedDepartmentChiefs(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get paged department chiefs request received, page: {}, size: {}", page, size);
        
        PageResponse<DepartmentChiefDTOs.DepartmentChiefResponse> response = 
                departmentChiefService.getDepartmentChiefsPaged(page, size);
        
        return ResponseEntity.ok(ApiResponse.success("Paged department chiefs retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentChiefDTOs.DepartmentChiefResponse>> updateDepartmentChief(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentChiefDTOs.DepartmentChiefUpdateRequest request) {
        log.info("Update department chief request received for ID: {}", id);
        
        DepartmentChiefDTOs.DepartmentChiefResponse response = 
                departmentChiefService.updateDepartmentChief(id, request);
        
        return ResponseEntity.ok(ApiResponse.success("Department chief updated successfully", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartmentChief(
            @PathVariable UUID id) {
        log.info("Delete department chief request received for ID: {}", id);
        
        departmentChiefService.deleteDepartmentChief(id);
        
        return ResponseEntity.ok(ApiResponse.success("Department chief deleted successfully"));
    }
    
    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}