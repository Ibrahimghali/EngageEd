package com.EngageEd.EngageEd.serviceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EngageEd.EngageEd.dto.EnrollmentDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.exception.AccessDeniedException;
import com.EngageEd.EngageEd.exception.ResourceNotFoundException;
import com.EngageEd.EngageEd.model.Enrollment;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.model.Subject;
import com.EngageEd.EngageEd.repository.EnrollmentRepository;
import com.EngageEd.EngageEd.service.EnrollmentService;
import com.EngageEd.EngageEd.service.StudentService;
import com.EngageEd.EngageEd.service.SubjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final SubjectService subjectService;
    
    @Override
    @Transactional
    public EnrollmentDTOs.EnrollmentResponse enrollStudentByCode(String subjectCode, Student student) {
        log.info("Enrolling student ID: {} in subject with code: {}", student.getId(), subjectCode);
        
        Subject subject = subjectService.getSubjectEntityByCode(subjectCode);
        
        // Verify that the subject is active
        if (!subject.isActive()) {
            throw new IllegalArgumentException("Cannot enroll in an inactive subject");
        }
        
        // Check if student is already enrolled
        if (isStudentEnrolledInSubject(student.getId(), subject.getId())) {
            throw new IllegalArgumentException("Student is already enrolled in this subject");
        }
        
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .subject(subject)
                .active(true)
                .build();
        
        enrollment = enrollmentRepository.save(enrollment);
        
        return toEnrollmentResponse(enrollment);
    }
    
    @Override
    @Transactional
    public EnrollmentDTOs.EnrollmentResponse enrollStudent(EnrollmentDTOs.EnrollmentRequest request) {
        log.info("Enrolling student ID: {} in subject ID: {}", request.getStudentId(), request.getSubjectId());
        
        Student student = studentService.getStudentEntityById(request.getStudentId());
        Subject subject = subjectService.getSubjectEntityById(request.getSubjectId());
        
        // Verify that the subject is active
        if (!subject.isActive()) {
            throw new IllegalArgumentException("Cannot enroll in an inactive subject");
        }
        
        // Check if student is already enrolled
        if (isStudentEnrolledInSubject(student.getId(), subject.getId())) {
            throw new IllegalArgumentException("Student is already enrolled in this subject");
        }
        
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .subject(subject)
                .active(true)
                .build();
        
        enrollment = enrollmentRepository.save(enrollment);
        
        return toEnrollmentResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentDTOs.EnrollmentResponse getEnrollmentById(UUID id) {
        log.info("Fetching enrollment with ID: {}", id);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + id));
        return toEnrollmentResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTOs.EnrollmentResponse> getEnrollmentsBySubject(UUID subjectId) {
        log.info("Fetching enrollments for subject ID: {}", subjectId);
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        
        return enrollmentRepository.findBySubject(subject).stream()
                .map(this::toEnrollmentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTOs.EnrollmentResponse> getActiveEnrollmentsBySubject(UUID subjectId) {
        log.info("Fetching active enrollments for subject ID: {}", subjectId);
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        
        return enrollmentRepository.findBySubjectAndActive(subject, true).stream()
                .map(this::toEnrollmentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTOs.EnrollmentResponse> getEnrollmentsByStudent(UUID studentId) {
        log.info("Fetching enrollments for student ID: {}", studentId);
        Student student = studentService.getStudentEntityById(studentId);
        
        return enrollmentRepository.findByStudent(student).stream()
                .map(this::toEnrollmentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTOs.EnrollmentResponse> getActiveEnrollmentsByStudent(UUID studentId) {
        log.info("Fetching active enrollments for student ID: {}", studentId);
        Student student = studentService.getStudentEntityById(studentId);
        
        return enrollmentRepository.findByStudentAndActive(student, true).stream()
                .map(this::toEnrollmentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResponse<EnrollmentDTOs.EnrollmentResponse> getEnrollmentsBySubjectPaged(UUID subjectId, int page, int size) {
        log.info("Fetching paged enrollments for subject ID: {}, page: {}, size: {}", subjectId, page, size);
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        
        Page<Enrollment> enrollmentPage = enrollmentRepository.findBySubject(subject, PageRequest.of(page, size));
        
        List<EnrollmentDTOs.EnrollmentResponse> content = enrollmentPage.getContent().stream()
                .map(this::toEnrollmentResponse)
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
        
        return toEnrollmentResponse(enrollment);
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
        Student student = studentService.getStudentEntityById(studentId);
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        
        return enrollmentRepository.existsByStudentAndSubjectAndActive(student, subject, true);
    }
    
    private EnrollmentDTOs.EnrollmentResponse toEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentDTOs.EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentName(enrollment.getStudent().getFullName())
                .studentEmail(enrollment.getStudent().getEmail())
                .subjectId(enrollment.getSubject().getId())
                .subjectName(enrollment.getSubject().getName())
                .subjectCode(enrollment.getSubject().getSubjectCode())
                .enrolledAt(enrollment.getCreatedAt())
                .active(enrollment.isActive())
                .build();
    }
}