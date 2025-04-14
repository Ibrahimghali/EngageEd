package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.EngageEd.EngageEd.dto.MaterialDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.model.Material;
import com.EngageEd.EngageEd.model.MaterialType;
import com.EngageEd.EngageEd.model.Professor;

/**
 * Service interface for material operations
 */
public interface MaterialService {
    
    /**
     * Upload a new material
     * 
     * @param request The upload request
     * @param file The actual file
     * @param professor The professor uploading the material
     * @return The created material DTO
     */
    MaterialDTOs.MaterialResponse uploadMaterial(
            MaterialDTOs.MaterialUploadRequest request, 
            MultipartFile file, 
            Professor professor);
    
    /**
     * Get a material by ID
     * 
     * @param id The material UUID
     * @return The material DTO
     */
    MaterialDTOs.MaterialResponse getMaterialById(UUID id);
    
    /**
     * Get the actual material entity
     * 
     * @param id The material UUID
     * @return The material entity
     */
    Material getMaterialEntityById(UUID id);
    
    /**
     * Get materials for a subject
     * 
     * @param subjectId The subject UUID
     * @return List of materials
     */
    List<MaterialDTOs.MaterialResponse> getMaterialsBySubject(UUID subjectId);
    
    /**
     * Get materials for a subject filtered by type
     * 
     * @param subjectId The subject UUID
     * @param type The material type
     * @return List of filtered materials
     */
    List<MaterialDTOs.MaterialResponse> getMaterialsBySubjectAndType(UUID subjectId, MaterialType type);
    
    /**
     * Search materials by title in a subject
     * 
     * @param subjectId The subject UUID
     * @param title The search query
     * @return List of matching materials
     */
    List<MaterialDTOs.MaterialResponse> searchMaterialsInSubject(UUID subjectId, String title);
    
    /**
     * Get paged materials for a subject
     * 
     * @param subjectId The subject UUID
     * @param page The page number (0-based)
     * @param size The page size
     * @return Paginated materials
     */
    PageResponse<MaterialDTOs.MaterialResponse> getMaterialsBySubjectPaged(UUID subjectId, int page, int size);
    
    /**
     * Update a material
     * 
     * @param id The material UUID
     * @param request The update request
     * @param professor The professor performing the update
     * @return The updated material DTO
     */
    MaterialDTOs.MaterialResponse updateMaterial(
            UUID id, 
            MaterialDTOs.MaterialUpdateRequest request, 
            Professor professor);
    
    /**
     * Replace the file for an existing material
     * 
     * @param id The material UUID
     * @param file The new file
     * @param professor The professor performing the update
     * @return The updated material DTO
     */
    MaterialDTOs.MaterialResponse replaceMaterialFile(UUID id, MultipartFile file, Professor professor);
    
    /**
     * Delete a material
     * 
     * @param id The material UUID
     * @param professor The professor performing the deletion
     */
    void deleteMaterial(UUID id, Professor professor);

    /**
     * Creates a new material for a subject
     * 
     * @param request the material creation request
     * @param subjectId the ID of the subject to add the material to
     * @param professor the professor creating the material
     * @return the created material as a response DTO
     */
    MaterialDTOs.MaterialResponse createMaterial(MaterialDTOs.MaterialCreationRequest request, UUID subjectId, Professor professor);
}