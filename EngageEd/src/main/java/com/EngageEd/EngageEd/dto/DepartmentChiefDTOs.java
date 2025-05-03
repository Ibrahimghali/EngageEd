package com.EngageEd.EngageEd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
        @Email(message = "Email should be valid")
        private String email;
        
        @NotBlank(message = "Full name is required")
        private String fullName;
        
        @NotBlank(message = "Department name is required")
        private String departmentName;
        
        @NotBlank(message = "Matricule is required")
        private String matricule;  // This field is required
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