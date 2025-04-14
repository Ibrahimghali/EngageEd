package com.EngageEd.EngageEd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTOs for subject operations
 */
public class SubjectDTOs {

    /**
     * Request for creating a new subject
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectCreationRequest {
        @NotBlank(message = "Subject name is required")
        @Size(min = 3, max = 100, message = "Subject name must be between 3 and 100 characters")
        private String name;
        
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;
        
        // Optional - system can generate if not provided
        @Size(min = 6, max = 10, message = "Subject code must be between 6 and 10 characters if provided")
        private String subjectCode;
    }
    
    /**
     * Response with subject data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectResponse {
        private UUID id;
        private String name;
        private String subjectCode;
        private String description;
        private UUID creatorId;
        private String creatorName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean active;
        private long studentsCount;
        private long materialsCount;
    }
    
    /**
     * Request for updating an existing subject
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectUpdateRequest {
        @Size(min = 3, max = 100, message = "Subject name must be between 3 and 100 characters")
        private String name;
        
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;
        
        private Boolean active;
    }
    
    /**
     * Request for enrolling in a subject using a code
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectEnrollmentRequest {
        @NotBlank(message = "Subject code is required")
        @Size(min = 6, max = 10, message = "Subject code must be between 6 and 10 characters")
        private String subjectCode;
    }
}