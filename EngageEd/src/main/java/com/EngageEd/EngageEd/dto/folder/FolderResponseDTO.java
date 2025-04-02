package com.EngageEd.EngageEd.dto.folder;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class FolderResponseDTO {
    private Long id;
    private String name;
    private Long createdBy;
    private Date createdAt;
}
