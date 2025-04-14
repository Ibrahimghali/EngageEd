package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.UUID;

import com.EngageEd.EngageEd.dto.EnrollmentDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Student;

/**
 * Service interface for enrollment operations
 */
public interface EnrollmentService {
    
    /**
     * Enroll a student in a subject using the subject code
     * 
     * @param subjectCode The subject code
     * @param student The student to enroll
     * @return The created enrollment DTO
     */
    EnrollmentDTOs.EnrollmentResponse enrollStudentByCode(String subjectCode, Student student);
    
    /**
     * Enroll a student in a subject (admin operation)
     * 
     * @param request The enrollment request
     * @return The created enrollment DTO
     */
    EnrollmentDTOs.EnrollmentResponse enrollStudent(EnrollmentDTOs.EnrollmentRequest request);
    
    /**
     * Get an enrollment by ID
     * 
     * @param id The enrollment UUID
     * @return The enrollment DTO
     */
    EnrollmentDTOs.EnrollmentResponse getEnrollmentById(UUID id);
    
    /**
     * Get enrollments for a subject
     * 
     * @param subjectId The subject UUID
     * @return List of enrollments
     */
    List<EnrollmentDTOs.EnrollmentResponse> getEnrollmentsBySubject(UUID subjectId);
    
    /**
     * Get active enrollments for a subject
     * 
     * @param subjectId The subject UUID
     * @return List of active enrollments
     */
    List<EnrollmentDTOs.EnrollmentResponse> getActiveEnrollmentsBySubject(UUID subjectId);
    
    /**
     * Get enrollments for a student
     * 
     * @param studentId The student UUID
     * @return List of enrollments
     */
    List<EnrollmentDTOs.EnrollmentResponse> getEnrollmentsByStudent(UUID studentId);
    
    /**
     * Get active enrollments for a student
     * 
     * @param studentId The student UUID
     * @return List of active enrollments
     */
    List<EnrollmentDTOs.EnrollmentResponse> getActiveEnrollmentsByStudent(UUID studentId);
    
    /**
     * Get paged enrollments for a subject
     * 
     * @param subjectId The subject UUID
     * @param page The page number (0-based)
     * @param size The page size
     * @return Paginated enrollments
     */
    PageResponse<EnrollmentDTOs.EnrollmentResponse> getEnrollmentsBySubjectPaged(UUID subjectId, int page, int size);
    
    /**
     * Update an enrollment
     * 
     * @param id The enrollment UUID
     * @param request The update request
     * @param professor The professor performing the update
     * @return The updated enrollment DTO
     */
    EnrollmentDTOs.EnrollmentResponse updateEnrollment(
            UUID id, 
            EnrollmentDTOs.EnrollmentUpdateRequest request, 
            Professor professor);
    
    /**
     * Delete an enrollment
     * 
     * @param id The enrollment UUID
     * @param professor The professor performing the deletion
     */
    void deleteEnrollment(UUID id, Professor professor);
    
    /**
     * Check if a student is enrolled in a subject
     * 
     * @param studentId The student UUID
     * @param subjectId The subject UUID
     * @return True if the student is enrolled
     */
    boolean isStudentEnrolledInSubject(UUID studentId, UUID subjectId);
}