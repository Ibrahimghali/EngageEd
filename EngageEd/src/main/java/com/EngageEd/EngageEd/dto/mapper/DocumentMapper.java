package com.EngageEd.EngageEd.dto.mapper;

import java.util.Date;

import com.EngageEd.EngageEd.dto.document.DocumentRequestDTO;
import com.EngageEd.EngageEd.dto.document.DocumentResponseDTO;
import com.EngageEd.EngageEd.model.Document;
import com.EngageEd.EngageEd.model.Folder;
import com.EngageEd.EngageEd.model.Professor;

public class DocumentMapper {
    public static DocumentResponseDTO toDTO(Document document) {
        return new DocumentResponseDTO(
            document.getId(),
            document.getTitle(),
            document.getDescription(),
            document.getType(),
            document.getFilePath(),
            document.getUploadedBy().getId(),
            document.getFolder().getId(),
            document.getUploadedAt()
        );
    }

    public static Document toEntity(DocumentRequestDTO dto, Professor uploadedBy, Folder folder) {
        return new Document(
            null,
            dto.getTitle(),
            dto.getDescription(),
            dto.getType(),
            dto.getFilePath(),
            uploadedBy,
            folder,
            new Date()
        );
    }
}
