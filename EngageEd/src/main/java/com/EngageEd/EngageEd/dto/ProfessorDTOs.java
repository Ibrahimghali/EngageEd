package com.EngageEd.EngageEd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * DTOs for professor operations
 */
public class ProfessorDTOs {

    /**
     * Request for professor registration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfessorRegistrationRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        private String email;
        
        @NotBlank(message = "Full name is required")
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        private String fullName;
        
        @Size(max = 100, message = "Specialization cannot exceed 100 characters")
        private String specialization;
        
        private String firebaseUid;
    }
    
    /**
     * Request for inviting a professor
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfessorInviteRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        private String email;
        
        @NotBlank(message = "Full name is required")
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        private String fullName;
        
        @Size(max = 100, message = "Specialization cannot exceed 100 characters")
        private String specialization;
    }
    
    /**
     * Response with professor data
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfessorResponse extends UserDTOs.UserResponse {
        private String specialization;
        private UUID registeredById;
        private String registeredByName;
        private long subjectsCount;
    }
    
    /**
     * Request for updating professor data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfessorUpdateRequest {
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        private String fullName;
        
        @Size(max = 100, message = "Specialization cannot exceed 100 characters")
        private String specialization;
        
        private Boolean active;
    }
}