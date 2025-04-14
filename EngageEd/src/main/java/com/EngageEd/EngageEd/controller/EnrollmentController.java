package com.EngageEd.EngageEd.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.EngageEd.EngageEd.dto.EnrollmentDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.service.EnrollmentService;
import com.EngageEd.EngageEd.service.ProfessorService;
import com.EngageEd.EngageEd.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Slf4j
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    
    @PostMapping("/enroll-by-code")
    public ResponseEntity<ApiResponse<EnrollmentDTOs.EnrollmentResponse>> enrollStudentByCode(
            @RequestParam String subjectCode,
            @RequestParam UUID studentId) {
        log.info("Enroll student by code request received for student ID: {} and subject code: {}", 
                studentId, subjectCode);
        
        Student student = studentService.getStudentEntityById(studentId);
        EnrollmentDTOs.EnrollmentResponse response = enrollmentService.enrollStudentByCode(subjectCode, student);
        
        return new ResponseEntity<>(ApiResponse.success("Student enrolled successfully", response), HttpStatus.CREATED);
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentDTOs.EnrollmentResponse>> enrollStudent(
            @Valid @RequestBody EnrollmentDTOs.EnrollmentRequest request) {
        log.info("Enroll student request received for student ID: {} and subject ID: {}", 
                request.getStudentId(), request.getSubjectId());
        
        EnrollmentDTOs.EnrollmentResponse response = enrollmentService.enrollStudent(request);
        
        return new ResponseEntity<>(ApiResponse.success("Student enrolled successfully", response), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentDTOs.EnrollmentResponse>> getEnrollment(
            @PathVariable UUID id) {
        log.info("Get enrollment request received for ID: {}", id);
        
        EnrollmentDTOs.EnrollmentResponse response = enrollmentService.getEnrollmentById(id);
        
        return ResponseEntity.ok(ApiResponse.success("Enrollment retrieved successfully", response));
    }
    
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<EnrollmentDTOs.EnrollmentResponse>>> getEnrollmentsBySubject(
            @PathVariable UUID subjectId) {
        log.info("Get enrollments by subject request received for ID: {}", subjectId);
        
        List<EnrollmentDTOs.EnrollmentResponse> response = enrollmentService.getEnrollmentsBySubject(subjectId);
        
        return ResponseEntity.ok(ApiResponse.success("Enrollments retrieved successfully", response));
    }
    
    @GetMapping("/subject/{subjectId}/active")
    public ResponseEntity<ApiResponse<List<EnrollmentDTOs.EnrollmentResponse>>> getActiveEnrollmentsBySubject(
            @PathVariable UUID subjectId) {
        log.info("Get active enrollments by subject request received for ID: {}", subjectId);
        
        List<EnrollmentDTOs.EnrollmentResponse> response = enrollmentService.getActiveEnrollmentsBySubject(subjectId);
        
        return ResponseEntity.ok(ApiResponse.success("Active enrollments retrieved successfully", response));
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentDTOs.EnrollmentResponse>>> getEnrollmentsByStudent(
            @PathVariable UUID studentId) {
        log.info("Get enrollments by student request received for ID: {}", studentId);
        
        List<EnrollmentDTOs.EnrollmentResponse> response = enrollmentService.getEnrollmentsByStudent(studentId);
        
        return ResponseEntity.ok(ApiResponse.success("Enrollments retrieved successfully", response));
    }
    
    @GetMapping("/student/{studentId}/active")
    public ResponseEntity<ApiResponse<List<EnrollmentDTOs.EnrollmentResponse>>> getActiveEnrollmentsByStudent(
            @PathVariable UUID studentId) {
        log.info("Get active enrollments by student request received for ID: {}", studentId);
        
        List<EnrollmentDTOs.EnrollmentResponse> response = enrollmentService.getActiveEnrollmentsByStudent(studentId);
        
        return ResponseEntity.ok(ApiResponse.success("Active enrollments retrieved successfully", response));
    }
    
    @GetMapping("/subject/{subjectId}/paged")
    public ResponseEntity<ApiResponse<PageResponse<EnrollmentDTOs.EnrollmentResponse>>> getPagedEnrollmentsBySubject(
            @PathVariable UUID subjectId,
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get paged enrollments request received for subject ID: {}, page: {}, size: {}", 
                subjectId, page, size);
        
        PageResponse<EnrollmentDTOs.EnrollmentResponse> response = 
                enrollmentService.getEnrollmentsBySubjectPaged(subjectId, page, size);
        
        return ResponseEntity.ok(ApiResponse.success("Paged enrollments retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentDTOs.EnrollmentResponse>> updateEnrollment(
            @PathVariable UUID id,
            @Valid @RequestBody EnrollmentDTOs.EnrollmentUpdateRequest request,
            @RequestParam UUID professorId) {
        log.info("Update enrollment request received for ID: {} by professor ID: {}", id, professorId);
        
        Professor professor = professorService.getProfessorEntityById(professorId);
        EnrollmentDTOs.EnrollmentResponse response = enrollmentService.updateEnrollment(id, request, professor);
        
        return ResponseEntity.ok(ApiResponse.success("Enrollment updated successfully", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEnrollment(
            @PathVariable UUID id,
            @RequestParam UUID professorId) {
        log.info("Delete enrollment request received for ID: {} by professor ID: {}", id, professorId);
        
        Professor professor = professorService.getProfessorEntityById(professorId);
        enrollmentService.deleteEnrollment(id, professor);
        
        return ResponseEntity.ok(ApiResponse.success("Enrollment deleted successfully"));
    }
    
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> isStudentEnrolledInSubject(
            @RequestParam UUID studentId,
            @RequestParam UUID subjectId) {
        log.info("Check if student is enrolled request received for student ID: {} and subject ID: {}", 
                studentId, subjectId);
        
        boolean isEnrolled = enrollmentService.isStudentEnrolledInSubject(studentId, subjectId);
        
        return ResponseEntity.ok(ApiResponse.success("Enrollment check completed", isEnrolled));
    }
}