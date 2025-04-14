package com.EngageEd.EngageEd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTOs for enrollment operations
 */
public class EnrollmentDTOs {

    /**
     * Request for enrolling a student in a subject
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrollmentRequest {
        @NotNull(message = "Student ID is required")
        private UUID studentId;
        
        @NotNull(message = "Subject ID is required")
        private UUID subjectId;
    }
    
    /**
     * Response with enrollment data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrollmentResponse {
        private UUID id;
        private UUID studentId;
        private String studentName;
        private String studentEmail;
        private UUID subjectId;
        private String subjectName;
        private String subjectCode;
        private LocalDateTime enrolledAt;
        private boolean active;
    }
    
    /**
     * Request for updating an enrollment
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrollmentUpdateRequest {
        private Boolean active;
    }
}