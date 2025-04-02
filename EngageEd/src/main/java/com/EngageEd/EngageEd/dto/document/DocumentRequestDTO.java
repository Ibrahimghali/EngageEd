package com.EngageEd.EngageEd.dto.document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentRequestDTO {
    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "File type is required")
    private String type;

    @NotBlank(message = "File path cannot be empty")
    private String filePath;

    @NotNull(message = "Folder ID is required")
    private Long folderId;
}
