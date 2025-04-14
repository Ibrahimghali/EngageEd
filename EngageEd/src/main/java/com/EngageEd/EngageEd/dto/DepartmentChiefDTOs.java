package com.EngageEd.EngageEd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.EngageEd.EngageEd.dto.UserDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * DTOs for department chief operations
 */
public class DepartmentChiefDTOs {

    /**
     * Request for department chief registration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentChiefRegistrationRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        private String email;
        
        @NotBlank(message = "Full name is required")
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        private String fullName;
        
        @NotBlank(message = "Department name is required")
        @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
        private String departmentName;
        
        @NotBlank(message = "Firebase UID is required")
        private String firebaseUid;
    }
    
    /**
     * Response with department chief data
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentChiefResponse extends UserDTOs.UserResponse {
        private String departmentName;
        private long managedProfessorsCount;
    }
    
    /**
     * Request for updating department chief data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentChiefUpdateRequest {
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        private String fullName;
        
        @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
        private String departmentName;
        
        private Boolean active;
    }
}