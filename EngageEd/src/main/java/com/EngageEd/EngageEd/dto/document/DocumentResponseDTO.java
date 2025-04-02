package com.EngageEd.EngageEd.dto.document;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String filePath;
    private Long uploadedById;
    private Long folderId;
    private Date uploadedAt;
}
