package com.EngageEd.EngageEd.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EngageEd.EngageEd.dto.EnrollmentDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.exception.ResourceNotFoundException;
import com.EngageEd.EngageEd.model.Enrollment;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.model.Subject;
import com.EngageEd.EngageEd.repository.EnrollmentRepository;
import com.EngageEd.EngageEd.repository.StudentRepository;
import com.EngageEd.EngageEd.repository.SubjectRepository;
import com.EngageEd.EngageEd.service.EnrollmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    
    @Override
    @Transactional
    public EnrollmentDTOs.EnrollmentResponse enrollStudentByCode(String subjectCode, Student student) {
        log.info("Enrolling student with ID: {} in subject with code: {}", student.getId(), subjectCode);
        
        // Find subject by code
        Subject subject = subjectRepository.findBySubjectCode(subjectCode)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with code: " + subjectCode));
        
        // Check if student is already enrolled
        if (enrollmentRepository.existsByStudentAndSubject(student, subject)) {
            throw new IllegalArgumentException("Student is already enrolled in this subject");
        }
        
        // Create new enrollment
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .subject(subject)
                .enrolledAt(LocalDateTime.now())
                .build();
        
        // Save enrollment
        enrollment = enrollmentRepository.save(enrollment);
        
        // Return response
        return mapToEnrollmentResponse(enrollment);
    }
    
    @Override
    @Transactional
    public EnrollmentDTOs.EnrollmentResponse enrollStudent(EnrollmentDTOs.EnrollmentRequest request) {
        log.info("Enrolling student ID: {} in subject ID: {}", request.getStudentId(), request.getSubjectId());
        
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + request.getSubjectId()));
        
        // Verify that the subject is active
        if (!subject.isActive()) {
            throw new IllegalArgumentException("Cannot enroll in an inactive subject");
        }
        
        // Check if student is already enrolled
        if (enrollmentRepository.existsByStudentAndSubject(student, subject)) {
            throw new IllegalArgumentException("Student is already enrolled in this subject");
        }
        
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .subject(subject)
                .enrolledAt(LocalDateTime.now())
                .active(true)
                .build();
        
        enrollment = enrollmentRepository.save(enrollment);
        
        return mapToEnrollmentResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentDTOs.EnrollmentResponse getEnrollmentById(UUID id) {
        log.info("Fetching enrollment with ID: {}", id);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + id));
        return mapToEnrollmentResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTOs.EnrollmentResponse> getEnrollmentsBySubject(UUID subjectId) {
        log.info("Fetching enrollments for subject ID: {}", subjectId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
        
        return enrollmentRepository.findBySubject(subject).stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTOs.EnrollmentResponse> getActiveEnrollmentsBySubject(UUID subjectId) {
        log.info("Fetching active enrollments for subject ID: {}", subjectId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
        
        return enrollmentRepository.findBySubjectAndActive(subject, true).stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTOs.EnrollmentResponse> getEnrollmentsByStudent(UUID studentId) {
        log.info("Fetching enrollments for student ID: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        
        return enrollmentRepository.findByStudent(student).stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTOs.EnrollmentResponse> getActiveEnrollmentsByStudent(UUID studentId) {
        log.info("Fetching active enrollments for student ID: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        
        return enrollmentRepository.findByStudentAndActive(student, true).stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResponse<EnrollmentDTOs.EnrollmentResponse> getEnrollmentsBySubjectPaged(UUID subjectId, int page, int size) {
        log.info("Fetching paged enrollments for subject ID: {}, page: {}, size: {}", subjectId, page, size);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
        
        Page<Enrollment> enrollmentPage = enrollmentRepository.findBySubject(subject, PageRequest.of(page, size));
        
        List<EnrollmentDTOs.EnrollmentResponse> content = enrollmentPage.getContent().stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
        
        return PageResponse.<EnrollmentDTOs.EnrollmentResponse>of(
                content, 
                page, 
                size, 
                enrollmentPage.getTotalElements());
    }

    @Override
    @Transactional
    public EnrollmentDTOs.EnrollmentResponse updateEnrollment(UUID id, EnrollmentDTOs.EnrollmentUpdateRequest request, 
            Professor professor) {
        log.info("Updating enrollment with ID: {} by professor ID: {}", id, professor.getId());
        
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + id));
        
        // Verify that the professor is the creator of the subject
        if (!enrollment.getSubject().getCreator().getId().equals(professor.getId())) {
            throw new AccessDeniedException("You do not have permission to update this enrollment");
        }
        
        if (request.getActive() != null) {
            enrollment.setActive(request.getActive());
        }
        
        enrollmentRepository.save(enrollment);
        
        return mapToEnrollmentResponse(enrollment);
    }

    @Override
    @Transactional
    public void deleteEnrollment(UUID id, Professor professor) {
        log.info("Deleting enrollment with ID: {} by professor ID: {}", id, professor.getId());
        
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + id));
        
        // Verify that the professor is the creator of the subject
        if (!enrollment.getSubject().getCreator().getId().equals(professor.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this enrollment");
        }
        
        enrollmentRepository.delete(enrollment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolledInSubject(UUID studentId, UUID subjectId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
        
        return enrollmentRepository.existsByStudentAndSubjectAndActive(student, subject, true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolledInSubject(String email, UUID subjectId) {
        log.info("Checking if student with email: {} is enrolled in subject with ID: {}", email, subjectId);
        
        // Find student
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));
                
        // Find subject
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
        
        // Check if enrollment exists
        return enrollmentRepository.existsByStudentAndSubject(student, subject);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTOs.EnrollmentResponse> getEnrollmentsByStudentEmail(String email) {
        log.info("Getting enrollments for student with email: {}", email);
        
        // Find student by email
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));
        
        // Get all enrollments
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        
        // Map to response DTOs
        return enrollments.stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void unenrollStudent(UUID enrollmentId, String email) {
        log.info("Unenrolling student with email: {} from enrollment: {}", email, enrollmentId);
        
        // Find enrollment
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + enrollmentId));
        
        // Check if the enrollment belongs to the student
        if (!enrollment.getStudent().getEmail().equals(email)) {
            throw new AccessDeniedException("You don't have permission to unenroll from this subject");
        }
        
        // Delete enrollment
        enrollmentRepository.delete(enrollment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EnrollmentDTOs.EnrollmentResponse getEnrollmentDetails(UUID enrollmentId, String email) {
        log.info("Getting enrollment details for ID: {} requested by: {}", enrollmentId, email);
        
        // Find enrollment
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + enrollmentId));
        
        // Check if the enrollment belongs to the student
        if (!enrollment.getStudent().getEmail().equals(email)) {
            throw new AccessDeniedException("You don't have permission to view this enrollment");
        }
        
        // Return response
        return mapToEnrollmentResponse(enrollment);
    }
    
    /**
     * Maps Enrollment entity to EnrollmentResponse DTO
     */
    private EnrollmentDTOs.EnrollmentResponse mapToEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentDTOs.EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentName(enrollment.getStudent().getFullName()) // Use getName() or appropriate method
                .subjectId(enrollment.getSubject().getId())
                .subjectName(enrollment.getSubject().getName()) // Use getName() or appropriate method
                .enrolledAt(enrollment.getEnrolledAt())
                .active(enrollment.isActive()) // Include if present in your DTO
                .build();
    }
}