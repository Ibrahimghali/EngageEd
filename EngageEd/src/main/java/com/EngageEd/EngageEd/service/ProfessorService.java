package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.ProfessorDTOs;
import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.dto.AuthenticationDTOs;

/**
 * Service interface for professor operations
 */
public interface ProfessorService {
    
    /**
     * Create a new professor
     * 
     * @param request The registration request
     * @param registeredBy The department chief registering the professor
     * @return The created professor entity
     */
    Professor createProfessor(ProfessorDTOs.ProfessorRegistrationRequest request, DepartmentChief registeredBy);
    
    /**
     * Register a new professor
     * 
     * @param request The registration request
     * @return The created professor DTO
     */
    ProfessorDTOs.ProfessorResponse registerProfessor(ProfessorDTOs.ProfessorRegistrationRequest request);
    
    /**
     * Invite a professor to join the platform (creates an inactive account)
     * 
     * @param request The invitation request
     * @param departmentChief The department chief creating the invitation
     * @return The created professor DTO
     */
    ProfessorDTOs.ProfessorResponse inviteProfessor(
            ProfessorDTOs.ProfessorInviteRequest request, 
            DepartmentChief departmentChief);
    
    /**
     * Get a professor by ID
     * 
     * @param id The professor UUID
     * @return The professor DTO
     */
    ProfessorDTOs.ProfessorResponse getProfessorById(UUID id);
    
    /**
     * Get a professor by email
     * 
     * @param email The professor's email
     * @return The professor DTO
     */
    ProfessorDTOs.ProfessorResponse getProfessorByEmail(String email);
    
    /**
     * Get a professor by Firebase UID
     * 
     * @param firebaseUid The Firebase UID
     * @return The professor DTO
     */
    ProfessorDTOs.ProfessorResponse getProfessorByFirebaseUid(String firebaseUid);
    
    /**
     * Get the actual professor entity
     * 
     * @param id The professor UUID
     * @return The professor entity
     */
    Professor getProfessorEntityById(UUID id);
    
    /**
     * Get all professors
     * 
     * @return List of all professors
     */
    List<ProfessorDTOs.ProfessorResponse> getAllProfessors();
    
    /**
     * Get professors registered by a specific department chief
     * 
     * @param departmentChiefId The department chief's UUID
     * @return List of professors
     */
    List<ProfessorDTOs.ProfessorResponse> getProfessorsByDepartmentChief(UUID departmentChiefId);
    
    /**
     * Get paged professors
     * 
     * @param page The page number (0-based)
     * @param size The page size
     * @return Paginated professors
     */
    PageResponse<ProfessorDTOs.ProfessorResponse> getProfessorsPaged(int page, int size);
    
    /**
     * Update a professor
     * 
     * @param id The professor UUID
     * @param request The update request
     * @return The updated professor DTO
     */
    ProfessorDTOs.ProfessorResponse updateProfessor(UUID id, ProfessorDTOs.ProfessorUpdateRequest request);
    
    /**
     * Delete a professor
     * 
     * @param id The professor UUID
     */
    void deleteProfessor(UUID id);

    /**
     * Create a new professor through self-registration
     * 
     * @param request The registration request
     * @return The created professor entity
     */
    Professor createProfessor(ProfessorDTOs.ProfessorRegistrationRequest request);

    /**
     * Find a professor by email
     * 
     * @param email The professor's email
     * @return The professor entity
     */
    @Transactional(readOnly = true)
    Professor findProfessorByEmail(String email);

    /**
     * Retrieves a Professor entity by email
     * @param email The email of the professor to retrieve
     * @return Professor entity
     * @throws ResourceNotFoundException if professor not found
     */
    Professor findProfessorEntityByEmail(String email);

    /**
     * Create a new professor from registration request
     * 
     * @param request The registration request
     * @param firebaseUid The Firebase UID
     * @return The created professor entity
     */
    Professor createProfessor(AuthenticationDTOs.RegistrationRequest request, String firebaseUid);
}