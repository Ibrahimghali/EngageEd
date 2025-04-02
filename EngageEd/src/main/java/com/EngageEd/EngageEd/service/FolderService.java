package com.EngageEd.EngageEd.service;

import java.util.List;

import com.EngageEd.EngageEd.dto.folder.FolderRequestDTO;
import com.EngageEd.EngageEd.dto.folder.FolderResponseDTO;
import com.EngageEd.EngageEd.model.Folder;

public interface FolderService {
    // New methods using DTOs
    FolderResponseDTO createFolder(FolderRequestDTO requestDTO, Long departmentChiefId);
    FolderResponseDTO renameFolder(Long folderId, String newName);
    List<FolderResponseDTO> getAllFolders();
    FolderResponseDTO getFolderById(Long folderId);
    
    // Legacy methods for backward compatibility
    Folder createFolder(String name, Long departmentChiefId);
    List<Folder> getAllFoldersLegacy();
    
    // Methods that don't need DTO conversion
    void deleteFolder(Long folderId);
}

