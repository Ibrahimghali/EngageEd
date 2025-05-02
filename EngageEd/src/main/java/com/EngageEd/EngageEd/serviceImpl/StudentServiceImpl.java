package com.EngageEd.EngageEd.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EngageEd.EngageEd.dto.AuthenticationDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.StudentDTOs;
import com.EngageEd.EngageEd.exception.ResourceNotFoundException;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.repository.StudentRepository;
import com.EngageEd.EngageEd.repository.SubjectRepository;
import com.EngageEd.EngageEd.service.StudentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    
    @Override
    @Transactional
    public Student createStudent(AuthenticationDTOs.RegistrationRequest request, String firebaseUid) {
        log.info("Creating student from authentication request with email: {}", request.getEmail());
        
        Student student = Student.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .matricule(request.getMatricule())  // Include matricule from the request
                .firebaseUid(firebaseUid)
                .role(UserRole.STUDENT)
                .active(true)
                .build();
        
        return studentRepository.save(student);
    }
    
    @Override
    @Transactional
    public Student createStudent(StudentDTOs.StudentRegistrationRequest request) {
        log.info("Creating student: {}", request.getEmail());
        
        Student student = Student.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                // Removed matricule as it doesn't exist in StudentRegistrationRequest
                .firebaseUid(request.getFirebaseUid())
                .role(UserRole.STUDENT)
                .active(true)
                .build();
        
        return studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Student findStudentEntityByEmail(String email) {
        log.info("Finding student entity by email: {}", email);
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));
    }
    
    // Helper method to get Student entity by Firebase UID (not part of the interface)
    private Student getStudentEntityByFirebaseUid(String firebaseUid) {
        return studentRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Firebase UID: " + firebaseUid));
    }
    
    @Override
    @Transactional(readOnly = true)
    public StudentDTOs.StudentResponse getStudentByEmail(String email) {
        log.info("Getting student response by email: {}", email);
        Student student = findStudentEntityByEmail(email);
        return toStudentResponse(student);
    }
    
    @Override
    @Transactional(readOnly = true)
    public StudentDTOs.StudentResponse getStudentById(UUID id) {
        log.info("Getting student response by ID: {}", id);
        Student student = getStudentEntityById(id);
        return toStudentResponse(student);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Student getStudentEntityById(UUID id) {
        log.info("Getting student entity by ID: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public StudentDTOs.StudentResponse getStudentByFirebaseUid(String firebaseUid) {
        log.info("Getting student response by Firebase UID: {}", firebaseUid);
        Student student = getStudentEntityByFirebaseUid(firebaseUid);
        return toStudentResponse(student);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentDTOs.StudentResponse> getAllStudents() {
        log.info("Getting all students");
        return studentRepository.findAll().stream()
                .map(this::toStudentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResponse<StudentDTOs.StudentResponse> getStudentsPaged(int page, int size) {
        log.info("Getting paged students, page: {}, size: {}", page, size);
        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(page, size));
        
        List<StudentDTOs.StudentResponse> content = studentPage.getContent().stream()
                .map(this::toStudentResponse)
                .collect(Collectors.toList());
        
        return PageResponse.<StudentDTOs.StudentResponse>of(
                content, 
                page, 
                size, 
                studentPage.getTotalElements());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentDTOs.StudentResponse> getStudentsBySubject(UUID subjectId) {
        log.info("Getting students by subject ID: {}", subjectId);
        // Verify subject exists but don't store in unused variable
        subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
        
        // Since we've removed enrollments, return empty list as placeholder
        return List.of();
    }
    
    @Override
    @Transactional
    public StudentDTOs.StudentResponse updateStudent(UUID id, StudentDTOs.StudentUpdateRequest request) {
        log.info("Updating student with ID: {}", id);
        
        Student student = getStudentEntityById(id);
        
        if (request.getFullName() != null) {
            student.setFullName(request.getFullName());
        }
        
        // Removed matricule update as it doesn't exist in StudentUpdateRequest
        
        if (request.getActive() != null) {
            student.setActive(request.getActive());
        }
        
        Student updatedStudent = studentRepository.save(student);
        return toStudentResponse(updatedStudent);
    }
    
    @Override
    @Transactional
    public void deleteStudent(UUID id) {
        log.info("Deleting student with ID: {}", id);
        
        Student student = getStudentEntityById(id);
        studentRepository.delete(student);
    }
  
    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findStudentByFirebaseUid(String firebaseUid) {
        log.info("Finding student by Firebase UID: {}", firebaseUid);
        return studentRepository.findByFirebaseUid(firebaseUid);
    }
    
    // Helper method to convert Student entity to StudentResponse DTO
    private StudentDTOs.StudentResponse toStudentResponse(Student student) {
        return StudentDTOs.StudentResponse.builder()
                .id(student.getId())
                .email(student.getEmail())
                .fullName(student.getFullName())
                // Removed matricule as it doesn't exist in Student
                .role(student.getRole())
                .active(student.isActive())
                .createdAt(student.getCreatedAt())
                .build();
    }
}