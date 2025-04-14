package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.StudentDTOs;
import com.EngageEd.EngageEd.model.Student;

/**
 * Service interface for student operations
 */
public interface StudentService {
    
    /**
     * Register a new student
     * 
     * @param request The registration request
     * @return The created student entity
     */
    Student createStudent(StudentDTOs.StudentRegistrationRequest request);
    
    /**
     * Get a student by ID
     * 
     * @param id The student UUID
     * @return The student DTO
     */
    StudentDTOs.StudentResponse getStudentById(UUID id);
    
    /**
     * Get a student by email
     * 
     * @param email The student's email
     * @return The student DTO
     */
    StudentDTOs.StudentResponse getStudentByEmail(String email);
    
    /**
     * Get a student by Firebase UID
     * 
     * @param firebaseUid The Firebase UID
     * @return The student DTO
     */
    StudentDTOs.StudentResponse getStudentByFirebaseUid(String firebaseUid);
    
    /**
     * Find a student entity by Firebase UID
     * 
     * @param firebaseUid The Firebase UID
     * @return Optional containing the student if found
     */
    Optional<Student> findStudentByFirebaseUid(String firebaseUid);
    
    /**
     * Get the actual student entity
     * 
     * @param id The student UUID
     * @return The student entity
     */
    Student getStudentEntityById(UUID id);
    
    /**
     * Get all students
     * 
     * @return List of all students
     */
    List<StudentDTOs.StudentResponse> getAllStudents();
    
    /**
     * Get students enrolled in a subject
     * 
     * @param subjectId The subject UUID
     * @return List of enrolled students
     */
    List<StudentDTOs.StudentResponse> getStudentsBySubject(UUID subjectId);
    
    /**
     * Get paged students
     * 
     * @param page The page number (0-based)
     * @param size The page size
     * @return Paginated students
     */
    PageResponse<StudentDTOs.StudentResponse> getStudentsPaged(int page, int size);
    
    /**
     * Update a student
     * 
     * @param id The student UUID
     * @param request The update request
     * @return The updated student DTO
     */
    StudentDTOs.StudentResponse updateStudent(UUID id, StudentDTOs.StudentUpdateRequest request);
    
    /**
     * Delete a student
     * 
     * @param id The student UUID
     */
    void deleteStudent(UUID id);

    Student findStudentEntityByEmail(String email);
}
