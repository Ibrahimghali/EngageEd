package com.EngageEd.EngageEd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

import com.EngageEd.EngageEd.model.MaterialType;

/**
 * DTOs for material operations
 */
public class MaterialDTOs {

    /**
     * Request for uploading a new material
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialUploadRequest {
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        private String title;
        
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;
        
        @NotNull(message = "Material type is required")
        private MaterialType type;
        
        @NotNull(message = "Subject ID is required")
        private UUID subjectId;
        
        // The actual file will be handled separately in the controller
    }
    
    /**
     * Response with material data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialResponse {
        private UUID id;
        private String title;
        private String description;
        private MaterialType type;
        private String fileUrl;
        private String fileName;
        private Long fileSize;
        private String contentType;
        private UUID subjectId;
        private String subjectName;
        private LocalDateTime uploadedAt;
    }
    
    /**
     * Request for updating an existing material
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialUpdateRequest {
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        private String title;
        
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;
        
        private MaterialType type;
    }
}