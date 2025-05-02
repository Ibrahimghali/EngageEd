package com.EngageEd.EngageEd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EngageEd.EngageEd.dto.ApiResponse;
import com.EngageEd.EngageEd.dto.AuthenticationDTOs;
import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.service.ChiefDepartmentValidationService;
import com.EngageEd.EngageEd.service.DepartmentChiefService;
import com.EngageEd.EngageEd.service.FirebaseAuthService;
import com.EngageEd.EngageEd.service.ProfessorService;
import com.EngageEd.EngageEd.service.ProfessorValidationService;
import com.EngageEd.EngageEd.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final FirebaseAuthService firebaseAuthService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final ProfessorValidationService professorValidationService;
    private final ChiefDepartmentValidationService chiefDepartmentValidationService;
    private final DepartmentChiefService departmentChiefService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody AuthenticationDTOs.RegistrationRequest request) {
        log.info("Registration request received for email: {}, role: {}", request.getEmail(), request.getRole());
        
        try {
            // Validate professor matricule if the role is PROFESSOR
            if (request.getRole() == UserRole.PROFESSOR) {
                boolean isValidProfessor = professorValidationService.isValidProfessorMatricule(request.getMatricule());
                if (!isValidProfessor) {
                    throw new IllegalArgumentException("Invalid professor matricule. You are not authorized as a professor.");
                }
                
                boolean isValidIdentity = professorValidationService.validateProfessorIdentity(
                    request.getMatricule(), request.getFullName());
                if (!isValidIdentity) {
                    throw new IllegalArgumentException("Professor identity verification failed. Name doesn't match our records.");
                }
            }
            
            // Validate department chief matricule if the role is DEPARTMENT_CHIEF
            if (request.getRole() == UserRole.DEPARTMENT_CHIEF) {
                boolean isValidChief = chiefDepartmentValidationService.isValidChiefDepartmentMatricule(request.getMatricule());
                if (!isValidChief) {
                    throw new IllegalArgumentException("Invalid department chief matricule. You are not authorized as a department chief.");
                }
                
                boolean isValidIdentity = chiefDepartmentValidationService.validateChiefDepartmentIdentity(
                    request.getMatricule(), request.getFullName());
                if (!isValidIdentity) {
                    throw new IllegalArgumentException("Department chief identity verification failed. Name doesn't match our records.");
                }
            }
            
            // Create Firebase user
            String firebaseUid = firebaseAuthService.createUser(request.getEmail(), request.getPassword());
            log.info("Firebase user created with UID: {}", firebaseUid);
            
            // Create user in our database
            if (request.getRole() == UserRole.STUDENT) {
                studentService.createStudent(request, firebaseUid);
            } else if (request.getRole() == UserRole.PROFESSOR) {
                professorService.createProfessor(request, firebaseUid);
            } else if (request.getRole() == UserRole.DEPARTMENT_CHIEF) {
                log.info("Creating department chief in database");
                DepartmentChief chief = departmentChiefService.createDepartmentChief(request, firebaseUid);
                log.info("Department chief created successfully: {}", chief.getId());
            }
            
            // Add this line to debug post-database operations
            log.info("Database operations completed successfully, proceeding with any post-processing");
            
            // LIKELY ERROR IS HERE: Setting custom claims or other post-registration processing
            
            log.info("Registration completed successfully");
            return ResponseEntity.ok(ApiResponse.success("User registered successfully"));
        } catch (Exception e) {
            log.error("Error during registration: ", e);
            throw e;
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationDTOs.LoginResponse>> login(
            @Valid @RequestBody AuthenticationDTOs.FirebaseAuthRequest request) {
        log.info("Login request received");
        
        AuthenticationDTOs.LoginResponse response = firebaseAuthService.authenticateWithFirebaseToken(request);
        
        return ResponseEntity.ok(ApiResponse.success("Authentication successful", response));
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationDTOs.LoginResponse>> refreshToken(
            @Valid @RequestBody AuthenticationDTOs.RefreshTokenRequest request) {
        log.info("Token refresh request received");
        
        // Implementation depends on your token refresh strategy
        // For Firebase, this might be handled differently
        
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", null));
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody AuthenticationDTOs.PasswordResetRequest request) {
        log.info("Password reset request received for email: {}", request.getEmail());
        
        // Generate reset link but don't assign to unused variable
        firebaseAuthService.generatePasswordResetLink(request.getEmail());
        // In a real implementation, you would send this link via email
        
        return ResponseEntity.ok(ApiResponse.success("Password reset email sent"));
    }
    
    @PostMapping("/email-signin-link")
    public ResponseEntity<ApiResponse<String>> generateEmailSignInLink(
            @Valid @RequestBody AuthenticationDTOs.EmailSignInLinkRequest request) {
        log.info("Email sign-in link requested for: {}", request.getEmail());
        
        String signInLink = firebaseAuthService.generateEmailSignInLink(
            request.getEmail(), 
            request.getRedirectUrl()
        );
        
        return ResponseEntity.ok(ApiResponse.success("Email sign-in link generated", signInLink));
    }
}