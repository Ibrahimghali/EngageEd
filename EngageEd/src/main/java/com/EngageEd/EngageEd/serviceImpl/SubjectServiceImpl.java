package com.EngageEd.EngageEd.serviceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.SubjectDTOs;
import com.EngageEd.EngageEd.exception.AccessDeniedException;
import com.EngageEd.EngageEd.exception.ResourceNotFoundException;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Subject;
import com.EngageEd.EngageEd.repository.EnrollmentRepository;
import com.EngageEd.EngageEd.repository.MaterialRepository;
import com.EngageEd.EngageEd.repository.SubjectRepository;
import com.EngageEd.EngageEd.service.ProfessorService;
import com.EngageEd.EngageEd.service.SubjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final MaterialRepository materialRepository;
    private final ProfessorService professorService;
    
    @Override
    @Transactional
    public Subject createSubject(SubjectDTOs.SubjectCreationRequest request, Professor professor) {
        log.info("Creating new subject: {} by professor ID: {}", request.getName(), professor.getId());
        
        String subjectCode = request.getSubjectCode();
        if (subjectCode == null || subjectCode.isEmpty()) {
            subjectCode = generateUniqueSubjectCode();
        } else if (subjectRepository.existsBySubjectCode(subjectCode)) {
            throw new IllegalArgumentException("Subject code already exists: " + subjectCode);
        }
        
        Subject subject = Subject.builder()
                .name(request.getName())
                .description(request.getDescription())
                .subjectCode(subjectCode)
                .creator(professor)
                .active(true)
                .build();
        
        return subjectRepository.save(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectDTOs.SubjectResponse getSubjectById(UUID id) {
        log.info("Fetching subject with ID: {}", id);
        return toSubjectResponse(getSubjectEntityById(id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Subject getSubjectEntityById(UUID id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public SubjectDTOs.SubjectResponse getSubjectByCode(String subjectCode) {
        log.info("Fetching subject with code: {}", subjectCode);
        return toSubjectResponse(getSubjectEntityByCode(subjectCode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Subject getSubjectEntityByCode(String subjectCode) {
        return subjectRepository.findBySubjectCode(subjectCode)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with code: " + subjectCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectDTOs.SubjectResponse> getAllSubjects() {
        log.info("Fetching all subjects");
        return subjectRepository.findAll().stream()
                .map(this::toSubjectResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SubjectDTOs.SubjectResponse> getActiveSubjects() {
        log.info("Fetching all active subjects");
        return subjectRepository.findByActive(true).stream()
                .map(this::toSubjectResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SubjectDTOs.SubjectResponse> getSubjectsByProfessor(Professor professor) {
        log.info("Fetching subjects for professor ID: {}", professor.getId());
        return subjectRepository.findByCreator(professor).stream()
                .map(this::toSubjectResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SubjectDTOs.SubjectResponse> getSubjectsByProfessor(UUID professorId) {
        log.info("Fetching subjects for professor ID: {}", professorId);
        Professor professor = professorService.getProfessorEntityById(professorId);
        return getSubjectsByProfessor(professor);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SubjectDTOs.SubjectResponse> getActiveSubjectsByProfessor(Professor professor) {
        log.info("Fetching active subjects for professor ID: {}", professor.getId());
        return subjectRepository.findByCreatorAndActive(professor, true).stream()
                .map(this::toSubjectResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SubjectDTOs.SubjectResponse> getActiveSubjectsByProfessor(UUID professorId) {
        log.info("Fetching active subjects for professor ID: {}", professorId);
        Professor professor = professorService.getProfessorEntityById(professorId);
        return getActiveSubjectsByProfessor(professor);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SubjectDTOs.SubjectResponse> searchSubjects(String name) {
        log.info("Searching subjects with name containing: {}", name);
        return subjectRepository.findByNameContainingIgnoreCaseAndActive(name, true).stream()
                .map(this::toSubjectResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResponse<SubjectDTOs.SubjectResponse> getSubjectsPaged(int page, int size) {
        log.info("Fetching paged subjects, page: {}, size: {}", page, size);
        Page<Subject> subjectPage = subjectRepository.findAll(PageRequest.of(page, size));
        
        List<SubjectDTOs.SubjectResponse> content = subjectPage.getContent().stream()
                .map(this::toSubjectResponse)
                .collect(Collectors.toList());
        
        return PageResponse.<SubjectDTOs.SubjectResponse>of(
                content, 
                page, 
                size, 
                subjectPage.getTotalElements());
    }

    @Override
    @Transactional
    public SubjectDTOs.SubjectResponse updateSubject(UUID id, SubjectDTOs.SubjectUpdateRequest request, Professor professor) {
        log.info("Updating subject with ID: {} by professor ID: {}", id, professor.getId());
        
        Subject subject = getSubjectEntityById(id);
        
        // Verify that the professor is the creator of the subject
        if (!subject.getCreator().getId().equals(professor.getId())) {
            throw new AccessDeniedException("You do not have permission to update this subject");
        }
        
        if (request.getName() != null) {
            subject.setName(request.getName());
        }
        
        if (request.getDescription() != null) {
            subject.setDescription(request.getDescription());
        }
        
        if (request.getActive() != null) {
            subject.setActive(request.getActive());
        }
        
        subjectRepository.save(subject);
        
        return toSubjectResponse(subject);
    }

    @Override
    @Transactional
    public void deleteSubject(UUID id, Professor professor) {
        log.info("Deleting subject with ID: {} by professor ID: {}", id, professor.getId());
        
        Subject subject = getSubjectEntityById(id);
        
        // Verify that the professor is the creator of the subject
        if (!subject.getCreator().getId().equals(professor.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this subject");
        }
        
        subjectRepository.delete(subject);
    }
    
    @Override
    public String generateUniqueSubjectCode() {
        String subjectCode;
        do {
            // Generate a random 8-character code
            subjectCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (subjectRepository.existsBySubjectCode(subjectCode));
        
        return subjectCode;
    }
    
    private SubjectDTOs.SubjectResponse toSubjectResponse(Subject subject) {
        long studentsCount = enrollmentRepository.countBySubjectAndActive(subject, true);
        long materialsCount = materialRepository.countBySubject(subject);
        
        return SubjectDTOs.SubjectResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .subjectCode(subject.getSubjectCode())
                .description(subject.getDescription())
                .creatorId(subject.getCreator().getId())
                .creatorName(subject.getCreator().getFullName())
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .active(subject.isActive())
                .studentsCount(studentsCount)
                .materialsCount(materialsCount)
                .build();
    }

    @Override
    @Transactional
    public SubjectDTOs.SubjectResponse createAndReturnSubject(SubjectDTOs.SubjectCreationRequest request, Professor professor) {
        log.info("Creating subject: {} by professor: {}", request.getName(), professor.getEmail());
        
        // Create and save the subject entity
        Subject subject = Subject.builder()
                .name(request.getName())
                .description(request.getDescription())
                .creator(professor)  // Changed from professor to creator
                .active(true)
                .build();
        
        Subject savedSubject = subjectRepository.save(subject);
        
        // Map to response DTO and return
        return toSubjectResponse(savedSubject);  // Changed from mapToSubjectResponse to toSubjectResponse
    }
}