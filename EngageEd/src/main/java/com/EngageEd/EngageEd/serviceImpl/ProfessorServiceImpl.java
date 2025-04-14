package com.EngageEd.EngageEd.serviceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.ProfessorDTOs;
import com.EngageEd.EngageEd.exception.ResourceAlreadyExistsException;
import com.EngageEd.EngageEd.exception.ResourceNotFoundException;
import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.repository.ProfessorRepository;
import com.EngageEd.EngageEd.repository.SubjectRepository;
import com.EngageEd.EngageEd.service.DepartmentChiefService;
import com.EngageEd.EngageEd.service.EmailService;
import com.EngageEd.EngageEd.service.FirebaseAuthService;
import com.EngageEd.EngageEd.service.ProfessorService;
import com.EngageEd.EngageEd.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;
    private final DepartmentChiefService departmentChiefService;
    private final EmailService emailService;
    private final FirebaseAuthService firebaseAuthService;
    private final UserService userService;
    
    @Override
    @Transactional
    public Professor createProfessor(ProfessorDTOs.ProfessorRegistrationRequest request, DepartmentChief registeredBy) {
        log.info("Creating new professor with email: {}", request.getEmail());
        
        // Check if email is already in use
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        
        Professor professor = Professor.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .specialization(request.getSpecialization())
                .firebaseUid(request.getFirebaseUid())
                .role(UserRole.PROFESSOR)
                .registeredBy(registeredBy)
                .active(true)
                .build();
        
        return professorRepository.save(professor);
    }
    
    @Override
    @Transactional
    public Professor createProfessor(ProfessorDTOs.ProfessorRegistrationRequest request) {
        log.info("Creating new professor through self-registration with email: {}", request.getEmail());
        
        // Check if email is already in use
        if (professorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        Professor professor = Professor.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .firebaseUid(request.getFirebaseUid())
                .role(UserRole.PROFESSOR)
                .registeredBy(null) // Self-registered professors don't have a department chief
                .active(true)
                .build();
        
        // Add any additional fields from the request if available
        if (request.getSpecialization() != null) {
            professor.setSpecialization(request.getSpecialization());
        }
        
        return professorRepository.save(professor);
    }
    
    @Override
    @Transactional
    public ProfessorDTOs.ProfessorResponse registerProfessor(ProfessorDTOs.ProfessorRegistrationRequest request) {
        log.info("Registering new professor with email: {}", request.getEmail());
        
        // Check if email is already in use
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        
        // If Firebase UID is not provided, create a new Firebase user
        if (request.getFirebaseUid() == null || request.getFirebaseUid().isEmpty()) {
            String firebaseUid = firebaseAuthService.createFirebaseUser(
                    request.getEmail(), 
                    null, // No password, will use email invite
                    request.getFullName(), 
                    UserRole.PROFESSOR);
            request.setFirebaseUid(firebaseUid);
        }
        
        Professor professor = Professor.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .specialization(request.getSpecialization())
                .firebaseUid(request.getFirebaseUid())
                .role(UserRole.PROFESSOR)
                .active(true)
                .build();
        
        professor = professorRepository.save(professor);
        
        return toProfessorResponse(professor);
    }
    
    @Override
    @Transactional
    public ProfessorDTOs.ProfessorResponse inviteProfessor(ProfessorDTOs.ProfessorInviteRequest request, DepartmentChief departmentChief) {
        log.info("Inviting professor with email: {}", request.getEmail());
        
        // Check if email is already in use
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        
        // Create the professor in our database (initially without Firebase UID)
        Professor professor = Professor.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .specialization(request.getSpecialization())
                .role(UserRole.PROFESSOR)
                .registeredBy(departmentChief)
                .active(false) // Inactive until they accept invitation
                .build();
        
        professor = professorRepository.save(professor);
        
        // Send invitation email
        String redirectUrl = "http://localhost:3000/professor/register?email=" + request.getEmail();
        String invitationLink = firebaseAuthService.generateEmailSignInLink(request.getEmail(), redirectUrl);
        
        emailService.sendProfessorInvitation(
                request.getEmail(),
                request.getFullName(),
                departmentChief.getFullName(),
                departmentChief.getDepartmentName(),
                invitationLink);
        
        return toProfessorResponse(professor);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessorDTOs.ProfessorResponse getProfessorById(UUID id) {
        log.info("Fetching professor with ID: {}", id);
        return toProfessorResponse(getProfessorEntityById(id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProfessorDTOs.ProfessorResponse getProfessorByEmail(String email) {
        log.info("Fetching professor with email: {}", email);
        Professor professor = professorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with email: " + email));
        return toProfessorResponse(professor);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProfessorDTOs.ProfessorResponse getProfessorByFirebaseUid(String firebaseUid) {
        log.info("Fetching professor with Firebase UID: {}", firebaseUid);
        Professor professor = professorRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with Firebase UID: " + firebaseUid));
        return toProfessorResponse(professor);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Professor getProfessorEntityById(UUID id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessorDTOs.ProfessorResponse> getAllProfessors() {
        log.info("Fetching all professors");
        return professorRepository.findAll().stream()
                .map(this::toProfessorResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProfessorDTOs.ProfessorResponse> getProfessorsByDepartmentChief(UUID departmentChiefId) {
        log.info("Fetching professors by department chief ID: {}", departmentChiefId);
        DepartmentChief departmentChief = departmentChiefService.getDepartmentChiefEntityById(departmentChiefId);
        return professorRepository.findByRegisteredBy(departmentChief).stream()
                .map(this::toProfessorResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProfessorDTOs.ProfessorResponse> getProfessorsPaged(int page, int size) {
        log.info("Fetching paged professors, page: {}, size: {}", page, size);
        Page<Professor> professorPage = professorRepository.findAll(PageRequest.of(page, size));
        
        List<ProfessorDTOs.ProfessorResponse> content = professorPage.getContent().stream()
                .map(this::toProfessorResponse)
                .collect(Collectors.toList());
        
        return PageResponse.<ProfessorDTOs.ProfessorResponse>of(
                content, 
                page, 
                size, 
                professorPage.getTotalElements());
    }

    @Override
    @Transactional
    public ProfessorDTOs.ProfessorResponse updateProfessor(UUID id, ProfessorDTOs.ProfessorUpdateRequest request) {
        log.info("Updating professor with ID: {}", id);
        
        Professor professor = getProfessorEntityById(id);
        
        if (request.getFullName() != null) {
            professor.setFullName(request.getFullName());
        }
        
        if (request.getSpecialization() != null) {
            professor.setSpecialization(request.getSpecialization());
        }
        
        if (request.getActive() != null) {
            professor.setActive(request.getActive());
        }
        
        professorRepository.save(professor);
        
        return toProfessorResponse(professor);
    }

    @Override
    @Transactional
    public void deleteProfessor(UUID id) {
        log.info("Deleting professor with ID: {}", id);
        Professor professor = getProfessorEntityById(id);
        
        // Delete Firebase user if it exists
        if (professor.getFirebaseUid() != null) {
            try {
                firebaseAuthService.deleteFirebaseUser(professor.getFirebaseUid());
            } catch (Exception e) {
                log.error("Error deleting Firebase user: {}", e.getMessage());
            }
        }
        
        professorRepository.delete(professor);
    }
    private ProfessorDTOs.ProfessorResponse toProfessorResponse(Professor professor) {
        long subjectsCount = subjectRepository.countByCreator(professor);
        
        return ProfessorDTOs.ProfessorResponse.builder()
                .id(professor.getId())
                .email(professor.getEmail())
                .fullName(professor.getFullName())
                .role(professor.getRole())
                .createdAt(professor.getCreatedAt())
                .active(professor.isActive())
                .specialization(professor.getSpecialization())
                .registeredById(professor.getRegisteredBy() != null ? professor.getRegisteredBy().getId() : null)
                .registeredByName(professor.getRegisteredBy() != null ? professor.getRegisteredBy().getFullName() : null)
                .subjectsCount(subjectsCount)
                .build();
    }
}