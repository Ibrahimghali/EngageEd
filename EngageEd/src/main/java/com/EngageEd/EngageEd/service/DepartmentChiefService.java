package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.UUID;

import com.EngageEd.EngageEd.dto.DepartmentChiefDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.model.DepartmentChief;

/**
 * Service interface for department chief operations
 */
public interface DepartmentChiefService {
    
    /**
     * Register a new department chief
     * 
     * @param request The registration request
     * @return The created department chief entity
     */
    DepartmentChief createDepartmentChief(DepartmentChiefDTOs.DepartmentChiefRegistrationRequest request);
    
    /**
     * Get a department chief by ID
     * 
     * @param id The department chief UUID
     * @return The department chief DTO
     */
    DepartmentChiefDTOs.DepartmentChiefResponse getDepartmentChiefById(UUID id);
    
    /**
     * Get the actual department chief entity
     * 
     * @param id The department chief UUID
     * @return The department chief entity
     */
    DepartmentChief getDepartmentChiefEntityById(UUID id);
    
    /**
     * Get a department chief by email
     * 
     * @param email The department chief's email
     * @return The department chief DTO
     */
    DepartmentChiefDTOs.DepartmentChiefResponse getDepartmentChiefByEmail(String email);
    
    /**
     * Get a department chief by Firebase UID
     * 
     * @param firebaseUid The Firebase UID
     * @return The department chief DTO
     */
    DepartmentChiefDTOs.DepartmentChiefResponse getDepartmentChiefByFirebaseUid(String firebaseUid);
    
    /**
     * Get all department chiefs
     * 
     * @return List of all department chiefs
     */
    List<DepartmentChiefDTOs.DepartmentChiefResponse> getAllDepartmentChiefs();
    
    /**
     * Get paged department chiefs
     * 
     * @param page The page number (0-based)
     * @param size The page size
     * @return Paginated department chiefs
     */
    PageResponse<DepartmentChiefDTOs.DepartmentChiefResponse> getDepartmentChiefsPaged(int page, int size);
    
    /**
     * Update a department chief
     * 
     * @param id The department chief UUID
     * @param request The update request
     * @return The updated department chief DTO
     */
    DepartmentChiefDTOs.DepartmentChiefResponse updateDepartmentChief(
            UUID id, 
            DepartmentChiefDTOs.DepartmentChiefUpdateRequest request);
    
    /**
     * Delete a department chief
     * 
     * @param id The department chief UUID
     */
    void deleteDepartmentChief(UUID id);
}
