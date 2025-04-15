package com.EngageEd.EngageEd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.EngageEd.EngageEd.model.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

/**
 * DTOs related to authentication operations
 */
public class AuthenticationDTOs {

    /**
     * Request for Firebase token authentication
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FirebaseAuthRequest {
        @NotBlank(message = "Firebase ID token is required")
        private String firebaseToken;
    }
    
    /**
     * Response for successful login
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private String jwtToken;
        private String refreshToken;
        private UserDTOs.UserResponse user;
    }
    
    /**
     * Request to refresh an authentication token
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenRequest {
        @NotBlank(message = "Refresh token is required")
        private String refreshToken;
    }
    
    /**
     * Request to reset a password
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordResetRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        private String email;
    }

    /**
     * Request for user registration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistrationRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;
        
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;
        
        @NotBlank(message = "Full name is required")
        private String fullName;
        
        @NotNull(message = "Matricule is required")
        private Integer matricule;
        
        @NotNull(message = "User role is required")
        private UserRole role;
    }

    /**
     * Request for email sign-in link
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailSignInLinkRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Valid email is required")
        private String email;
        
        @NotBlank(message = "Redirect URL is required")
        private String redirectUrl;
    }
}