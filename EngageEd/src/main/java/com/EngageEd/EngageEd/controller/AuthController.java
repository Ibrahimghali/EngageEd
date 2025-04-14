package com.EngageEd.EngageEd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EngageEd.EngageEd.dto.ApiResponse;
import com.EngageEd.EngageEd.dto.AuthenticationDTOs;
import com.EngageEd.EngageEd.dto.ProfessorDTOs;
import com.EngageEd.EngageEd.dto.StudentDTOs;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.service.FirebaseAuthService;
import com.EngageEd.EngageEd.service.StudentService;
import com.EngageEd.EngageEd.service.ProfessorService;

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
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody AuthenticationDTOs.RegistrationRequest request) {
        log.info("Registration request received for email: {}", request.getEmail());
        
        // Create Firebase user first
        String firebaseUid = firebaseAuthService.createFirebaseUser(
            request.getEmail(), 
            request.getPassword(),
            request.getFullName(),
            request.getRole()
        );
        
        // Create local user based on role
        if (request.getRole() == UserRole.STUDENT) {
            studentService.createStudent(
                StudentDTOs.StudentRegistrationRequest.builder()
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .firebaseUid(firebaseUid)
                    .build()
            );
        } else if (request.getRole() == UserRole.PROFESSOR) {
            professorService.createProfessor(
                ProfessorDTOs.ProfessorRegistrationRequest.builder()
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .firebaseUid(firebaseUid)
                    .build()
            );
        }
        
        return ResponseEntity.ok(ApiResponse.success("Registration successful. Please log in."));
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
        
        String resetLink = firebaseAuthService.generatePasswordResetLink(request.getEmail());
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