package com.EngageEd.EngageEd.serviceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EngageEd.EngageEd.dto.AuthenticationDTOs;
import com.EngageEd.EngageEd.dto.DepartmentChiefDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.exception.ResourceAlreadyExistsException;
import com.EngageEd.EngageEd.exception.ResourceNotFoundException;
import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.repository.DepartmentChiefRepository;
import com.EngageEd.EngageEd.repository.ProfessorRepository;
import com.EngageEd.EngageEd.service.DepartmentChiefService;
import com.EngageEd.EngageEd.service.FirebaseAuthService;
import com.EngageEd.EngageEd.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentChiefServiceImpl implements DepartmentChiefService {

    private final DepartmentChiefRepository departmentChiefRepository;
    private final ProfessorRepository professorRepository;
    private final UserService userService;
    private final FirebaseAuthService firebaseAuthService;
    
    @Override
    @Transactional
    public DepartmentChief createDepartmentChief(DepartmentChiefDTOs.DepartmentChiefRegistrationRequest request) {
        log.info("Creating new department chief with email: {}", request.getEmail());
        
        // Check if email is already in use
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + request.getEmail());
        }
        
        // Convert matricule from String to Integer
        Integer matriculeInt;
        try {
            matriculeInt = Integer.parseInt(request.getMatricule());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Matricule must be a valid number", e);
        }
        
        DepartmentChief departmentChief = DepartmentChief.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .departmentName(request.getDepartmentName())
                .matricule(matriculeInt)
                .role(UserRole.DEPARTMENT_CHIEF)
                .active(true)
                .build();
        
        return departmentChiefRepository.save(departmentChief);
    }

    @Override
    @Transactional
    public DepartmentChief createDepartmentChief(AuthenticationDTOs.RegistrationRequest request, String firebaseUid) {
        log.info("Creating department chief from auth flow with email: {}", request.getEmail());
        
        // Check if email is already in use
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + request.getEmail());
        }
        
        DepartmentChief departmentChief = DepartmentChief.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .matricule(request.getMatricule())
                .firebaseUid(firebaseUid)
                .role(UserRole.DEPARTMENT_CHIEF)
                .departmentName("Computer Science Department") // Add default department name
                .active(true)
                .build();
        
        return departmentChiefRepository.save(departmentChief);
    }

    @Transactional
    public DepartmentChief createDepartmentChiefWithFirebaseUid(
            DepartmentChiefDTOs.DepartmentChiefRegistrationRequest request, 
            String firebaseUid) {
        
        // Check if email is already in use
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + request.getEmail());
        }
        
        // Convert matricule from String to Integer
        Integer matriculeInt;
        try {
            matriculeInt = Integer.parseInt(request.getMatricule());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Matricule must be a valid number", e);
        }
        
        // Use a default department name if none is provided
        String departmentName = request.getDepartmentName();
        if (departmentName == null || departmentName.trim().isEmpty()) {
            departmentName = "Computer Science Department"; // Default value
        }
        
        // Use the builder pattern consistent with other methods
        DepartmentChief departmentChief = DepartmentChief.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .departmentName(departmentName)  // Use the validated department name
                .matricule(matriculeInt)
                .firebaseUid(firebaseUid)
                .role(UserRole.DEPARTMENT_CHIEF)
                .active(true)
                .build();
        
        return departmentChiefRepository.save(departmentChief);
    }

    

    @Override
    @Transactional(readOnly = true)
    public DepartmentChiefDTOs.DepartmentChiefResponse getDepartmentChiefById(UUID id) {
        log.info("Fetching department chief with ID: {}", id);
        return toDepartmentChiefResponse(getDepartmentChiefEntityById(id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentChief getDepartmentChiefEntityById(UUID id) {
        return departmentChiefRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department chief not found with ID: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentChiefDTOs.DepartmentChiefResponse getDepartmentChiefByEmail(String email) {
        log.info("Fetching department chief with email: {}", email);
        DepartmentChief departmentChief = departmentChiefRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Department chief not found with email: " + email));
        return toDepartmentChiefResponse(departmentChief);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentChiefDTOs.DepartmentChiefResponse getDepartmentChiefByFirebaseUid(String firebaseUid) {
        log.info("Fetching department chief with Firebase UID: {}", firebaseUid);
        DepartmentChief departmentChief = departmentChiefRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new ResourceNotFoundException("Department chief not found with Firebase UID: " + firebaseUid));
        return toDepartmentChiefResponse(departmentChief);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentChiefDTOs.DepartmentChiefResponse> getAllDepartmentChiefs() {
        log.info("Fetching all department chiefs");
        return departmentChiefRepository.findAll().stream()
                .map(this::toDepartmentChiefResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResponse<DepartmentChiefDTOs.DepartmentChiefResponse> getDepartmentChiefsPaged(int page, int size) {
        log.info("Fetching paged department chiefs, page: {}, size: {}", page, size);
        Page<DepartmentChief> departmentChiefPage = departmentChiefRepository.findAll(PageRequest.of(page, size));
        
        List<DepartmentChiefDTOs.DepartmentChiefResponse> content = departmentChiefPage.getContent().stream()
                .map(this::toDepartmentChiefResponse)
                .collect(Collectors.toList());
        
        return PageResponse.<DepartmentChiefDTOs.DepartmentChiefResponse>of(
                content, 
                page, 
                size, 
                departmentChiefPage.getTotalElements());
    }

    @Override
    @Transactional
    public DepartmentChiefDTOs.DepartmentChiefResponse updateDepartmentChief(UUID id, DepartmentChiefDTOs.DepartmentChiefUpdateRequest request) {
        log.info("Updating department chief with ID: {}", id);
        
        DepartmentChief departmentChief = getDepartmentChiefEntityById(id);
        
        if (request.getFullName() != null) {
            departmentChief.setFullName(request.getFullName());
        }
        
        if (request.getDepartmentName() != null) {
            departmentChief.setDepartmentName(request.getDepartmentName());
        }
        
        if (request.getActive() != null) {
            departmentChief.setActive(request.getActive());
        }
        
        departmentChiefRepository.save(departmentChief);
        
        return toDepartmentChiefResponse(departmentChief);
    }

    @Override
    @Transactional
    public void deleteDepartmentChief(UUID id) {
        log.info("Deleting department chief with ID: {}", id);
        
        // Find the department chief first
        DepartmentChief chief = departmentChiefRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department chief not found with ID: " + id));
        
        // Get the Firebase UID before deleting
        String firebaseUid = chief.getFirebaseUid();
        
        // Delete from the database
        departmentChiefRepository.deleteById(id);
        
        // Also delete the user from Firebase Auth
        try {
            firebaseAuthService.deleteUser(firebaseUid);
            log.info("Firebase user deleted for UID: {}", firebaseUid);
        } catch (Exception e) {
            log.error("Error deleting Firebase user: {}", e.getMessage());
            // Continue with the operation even if Firebase deletion fails
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentChief findDepartmentChiefEntityByEmail(String email) {
        log.info("Finding department chief entity with email: {}", email);
        return departmentChiefRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Department chief not found with email: " + email));
    }

    private DepartmentChiefDTOs.DepartmentChiefResponse toDepartmentChiefResponse(DepartmentChief departmentChief) {
        long managedProfessorsCount = professorRepository.countByRegisteredBy(departmentChief);
        
        return DepartmentChiefDTOs.DepartmentChiefResponse.builder()
                .id(departmentChief.getId())
                .email(departmentChief.getEmail())
                .fullName(departmentChief.getFullName())
                .role(departmentChief.getRole())
                .createdAt(departmentChief.getCreatedAt())
                .active(departmentChief.isActive())
                .departmentName(departmentChief.getDepartmentName())
                .managedProfessorsCount(managedProfessorsCount)
                .build();
    }
}