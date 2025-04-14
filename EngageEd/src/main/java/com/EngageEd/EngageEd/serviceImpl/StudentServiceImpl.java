package com.EngageEd.EngageEd.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.StudentDTOs;
import com.EngageEd.EngageEd.exception.ResourceAlreadyExistsException;
import com.EngageEd.EngageEd.exception.ResourceNotFoundException;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.model.Subject;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.repository.EnrollmentRepository;
import com.EngageEd.EngageEd.repository.StudentRepository;
import com.EngageEd.EngageEd.service.FirebaseAuthService;
import com.EngageEd.EngageEd.service.StudentService;
import com.EngageEd.EngageEd.service.SubjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SubjectService subjectService;
    private final FirebaseAuthService firebaseAuthService;
    
    @Override
    @Transactional
    public Student createStudent(StudentDTOs.StudentRegistrationRequest request) {
        log.info("Creating new student with email: {}", request.getEmail());
        
        // Check if email is already in use
        if (studentRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        
        // If Firebase UID is not provided, create a new Firebase user
        if (request.getFirebaseUid() == null || request.getFirebaseUid().isEmpty()) {
            String firebaseUid = firebaseAuthService.createFirebaseUser(
                    request.getEmail(), 
                    null, // No password, will use email invite
                    request.getFullName(), 
                    UserRole.STUDENT);
            request.setFirebaseUid(firebaseUid);
        }
        
        Student student = Student.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .firebaseUid(request.getFirebaseUid())
                .role(UserRole.STUDENT)
                .active(true)
                .build();
        
        return studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTOs.StudentResponse getStudentById(UUID id) {
        log.info("Fetching student with ID: {}", id);
        return toStudentResponse(getStudentEntityById(id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public StudentDTOs.StudentResponse getStudentByEmail(String email) {
        log.info("Fetching student with email: {}", email);
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));
        return toStudentResponse(student);
    }
    
    @Override
    @Transactional(readOnly = true)
    public StudentDTOs.StudentResponse getStudentByFirebaseUid(String firebaseUid) {
        log.info("Fetching student with Firebase UID: {}", firebaseUid);
        Student student = studentRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Firebase UID: " + firebaseUid));
        return toStudentResponse(student);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findStudentByFirebaseUid(String firebaseUid) {
        return studentRepository.findByFirebaseUid(firebaseUid);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Student getStudentEntityById(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTOs.StudentResponse> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll().stream()
                .map(this::toStudentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentDTOs.StudentResponse> getStudentsBySubject(UUID subjectId) {
        log.info("Fetching students for subject ID: {}", subjectId);
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        
        return enrollmentRepository.findBySubjectAndActive(subject, true).stream()
                .map(enrollment -> enrollment.getStudent())
                .map(this::toStudentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResponse<StudentDTOs.StudentResponse> getStudentsPaged(int page, int size) {
        log.info("Fetching paged students, page: {}, size: {}", page, size);
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
    @Transactional
    public StudentDTOs.StudentResponse updateStudent(UUID id, StudentDTOs.StudentUpdateRequest request) {
        log.info("Updating student with ID: {}", id);
        
        Student student = getStudentEntityById(id);
        
        if (request.getFullName() != null) {
            student.setFullName(request.getFullName());
        }
        
        if (request.getActive() != null) {
            student.setActive(request.getActive());
        }
        
        studentRepository.save(student);
        
        return toStudentResponse(student);
    }

    @Override
    @Transactional
    public void deleteStudent(UUID id) {
        log.info("Deleting student with ID: {}", id);
        Student student = getStudentEntityById(id);
        
        // Delete Firebase user if it exists
        if (student.getFirebaseUid() != null) {
            try {
                firebaseAuthService.deleteFirebaseUser(student.getFirebaseUid());
            } catch (Exception e) {
                log.error("Error deleting Firebase user", e);
                // Continue with deletion even if Firebase user couldn't be deleted
            }
        }
        
        studentRepository.delete(student);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Student findStudentEntityByEmail(String email) {
        log.info("Finding student entity with email: {}", email);
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));
    }
    
    private StudentDTOs.StudentResponse toStudentResponse(Student student) {
        long enrolledSubjectsCount = enrollmentRepository.countByStudentAndActive(student, true);
        
        return StudentDTOs.StudentResponse.builder()
                .id(student.getId())
                .email(student.getEmail())
                .fullName(student.getFullName())
                .role(student.getRole())
                .createdAt(student.getCreatedAt())
                .active(student.isActive())
                .enrolledSubjectsCount(enrolledSubjectsCount)
                .build();
    }
}