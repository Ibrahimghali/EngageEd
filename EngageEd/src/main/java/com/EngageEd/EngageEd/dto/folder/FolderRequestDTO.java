package com.EngageEd.EngageEd.dto.folder;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FolderRequestDTO {
    @NotBlank(message = "Folder name cannot be empty")
    private String name;
}

