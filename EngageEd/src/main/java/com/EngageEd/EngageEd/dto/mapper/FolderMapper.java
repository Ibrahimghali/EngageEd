package com.EngageEd.EngageEd.dto.mapper;

import com.EngageEd.EngageEd.dto.folder.FolderResponseDTO;
import com.EngageEd.EngageEd.model.Folder;

public class FolderMapper {
    public static FolderResponseDTO toDTO(Folder folder) {
        return new FolderResponseDTO(folder.getId(), folder.getName(), folder.getCreatedBy().getId(), folder.getCreatedAt());
    }
}
