package com.EngageEd.EngageEd.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

import com.EngageEd.EngageEd.model.UserRole;

/**
 * DTOs for base user operations that apply to all user types
 */
public class UserDTOs {

    /**
     * Base request for user registration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRegistrationRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        private String email;
        
        @NotBlank(message = "Full name is required")
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        private String fullName;
        
        @NotBlank(message = "Firebase UID is required")
        private String firebaseUid;
        
        @NotNull(message = "User role is required")
        private UserRole role;
    }
    
    /**
     * Base response for user data
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private UUID id;
        private String email;
        private String fullName;
        private UserRole role;
        private LocalDateTime createdAt;
        private boolean active;
    }
    
    /**
     * Base request for updating user data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateRequest {
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        private String fullName;
        
        private Boolean active;
    }
}