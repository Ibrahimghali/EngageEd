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
import com.EngageEd.EngageEd.dto.StudentDTOs;
import com.EngageEd.EngageEd.dto.SubjectDTOs; // Add this import
import com.EngageEd.EngageEd.service.StudentService;
import com.EngageEd.EngageEd.service.SubjectService; // Add this import

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;
    private final SubjectService subjectService; // Add this line
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<StudentDTOs.StudentResponse>> registerStudent(
            @Valid @RequestBody StudentDTOs.StudentRegistrationRequest request) {
        log.info("Register student request received: {}", request.getEmail());
        
        studentService.createStudent(request);
        StudentDTOs.StudentResponse response = studentService.getStudentByEmail(request.getEmail());
        
        return new ResponseEntity<>(ApiResponse.success("Student registered successfully", response), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTOs.StudentResponse>> getStudent(
            @PathVariable UUID id) {
        log.info("Get student request received for ID: {}", id);
        
        StudentDTOs.StudentResponse response = studentService.getStudentById(id);
        
        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentDTOs.StudentResponse>>> getAllStudents() {
        log.info("Get all students request received");
        
        List<StudentDTOs.StudentResponse> response = studentService.getAllStudents();
        
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", response));
    }
    
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<StudentDTOs.StudentResponse>>> getStudentsBySubject(
            @PathVariable UUID subjectId) {
        log.info("Get students by subject request received for ID: {}", subjectId);
        
        List<StudentDTOs.StudentResponse> response = studentService.getStudentsBySubject(subjectId);
        
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", response));
    }
    
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<PageResponse<StudentDTOs.StudentResponse>>> getPagedStudents(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get paged students request received, page: {}, size: {}", page, size);
        
        PageResponse<StudentDTOs.StudentResponse> response = 
                studentService.getStudentsPaged(page, size);
        
        return ResponseEntity.ok(ApiResponse.success("Paged students retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTOs.StudentResponse>> updateStudent(
            @PathVariable UUID id,
            @Valid @RequestBody StudentDTOs.StudentUpdateRequest request) {
        log.info("Update student request received for ID: {}", id);
        
        StudentDTOs.StudentResponse response = studentService.updateStudent(id, request);
        
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(
            @PathVariable UUID id) {
        log.info("Delete student request received for ID: {}", id);
        
        studentService.deleteStudent(id);
        
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully"));
    }

    @GetMapping("/all-subjects")
    public ResponseEntity<ApiResponse<List<SubjectDTOs.SubjectResponse>>> getAllSubjectsForStudent(
            Authentication authentication) {
        
        log.info("Getting all subjects for authenticated student");
        
        // Simply call the service to get all active subjects
        List<SubjectDTOs.SubjectResponse> subjects = subjectService.getActiveSubjects();
        
        return ResponseEntity.ok(ApiResponse.success("All subjects retrieved successfully", subjects));
    }
}