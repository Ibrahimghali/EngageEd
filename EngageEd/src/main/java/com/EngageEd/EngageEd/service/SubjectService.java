package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.UUID;

import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.SubjectDTOs;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Subject;

/**
 * Service interface for subject operations
 */
public interface SubjectService {
    
    /**
     * Create a new subject
     * 
     * @param request The creation request
     * @param professor The professor creating the subject
     * @return The created subject entity
     */
    Subject createSubject(SubjectDTOs.SubjectCreationRequest request, Professor professor);
    
    /**
     * Creates a new subject and returns it as a response DTO
     * 
     * @param request the subject creation request
     * @param professor the professor creating the subject
     * @return the created subject as a response DTO
     */
    SubjectDTOs.SubjectResponse createAndReturnSubject(SubjectDTOs.SubjectCreationRequest request, Professor professor);
    
    /**
     * Get a subject by ID
     * 
     * @param id The subject UUID
     * @return The subject DTO
     */
    SubjectDTOs.SubjectResponse getSubjectById(UUID id);
    
    /**
     * Get the actual subject entity
     * 
     * @param id The subject UUID
     * @return The subject entity
     */
    Subject getSubjectEntityById(UUID id);
    
    /**
     * Get a subject by its code
     * 
     * @param subjectCode The unique subject code
     * @return The subject DTO
     */
    SubjectDTOs.SubjectResponse getSubjectByCode(String subjectCode);
    
    /**
     * Get the actual subject entity by code
     * 
     * @param subjectCode The unique subject code
     * @return The subject entity
     */
    Subject getSubjectEntityByCode(String subjectCode);
    
    /**
     * Get all subjects
     * 
     * @return List of all subjects
     */
    List<SubjectDTOs.SubjectResponse> getAllSubjects();
    
    /**
     * Get all active subjects
     * 
     * @return List of active subjects
     */
    List<SubjectDTOs.SubjectResponse> getActiveSubjects();
    
    /**
     * Get subjects created by a specific professor
     * 
     * @param professor The professor entity
     * @return List of subjects
     */
    List<SubjectDTOs.SubjectResponse> getSubjectsByProfessor(Professor professor);
    
    /**
     * Get subjects created by a specific professor
     * 
     * @param professorId The professor's UUID
     * @return List of subjects
     */
    List<SubjectDTOs.SubjectResponse> getSubjectsByProfessor(UUID professorId);
    
    /**
     * Get active subjects created by a specific professor
     * 
     * @param professor The professor entity
     * @return List of active subjects
     */
    List<SubjectDTOs.SubjectResponse> getActiveSubjectsByProfessor(Professor professor);
    
    /**
     * Get active subjects created by a specific professor
     * 
     * @param professorId The professor's UUID
     * @return List of active subjects
     */
    List<SubjectDTOs.SubjectResponse> getActiveSubjectsByProfessor(UUID professorId);
    
    /**
     * Search subjects by name
     * 
     * @param name The search query
     * @return List of matching subjects
     */
    List<SubjectDTOs.SubjectResponse> searchSubjects(String name);
    
    /**
     * Get paged subjects
     * 
     * @param page The page number (0-based)
     * @param size The page size
     * @return Paginated subjects
     */
    PageResponse<SubjectDTOs.SubjectResponse> getSubjectsPaged(int page, int size);
    
    /**
     * Update a subject
     * 
     * @param id The subject UUID
     * @param request The update request
     * @param professor The professor performing the update
     * @return The updated subject DTO
     */
    SubjectDTOs.SubjectResponse updateSubject(
            UUID id, 
            SubjectDTOs.SubjectUpdateRequest request, 
            Professor professor);
    
    /**
     * Delete a subject
     * 
     * @param id The subject UUID
     * @param professor The professor performing the deletion
     */
    void deleteSubject(UUID id, Professor professor);
    
    /**
     * Generate a unique subject code
     * 
     * @return A new unique subject code
     */
    String generateUniqueSubjectCode();
    
    /**
     * Get all subjects that are available for a student to enroll in
     * (subjects where the student is not already enrolled)
     * 
     * @param studentEmail the email of the student
     * @return list of subjects available for enrollment
     */
    List<SubjectDTOs.SubjectResponse> getAvailableSubjectsForStudent(String studentEmail);
}
