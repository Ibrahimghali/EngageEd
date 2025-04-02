package com.EngageEd.EngageEd.serviceImpl;

import com.EngageEd.EngageEd.dto.folder.FolderRequestDTO;
import com.EngageEd.EngageEd.dto.folder.FolderResponseDTO;
import com.EngageEd.EngageEd.dto.mapper.FolderMapper;
import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Folder;
import com.EngageEd.EngageEd.repository.DepartmentChiefRepository;
import com.EngageEd.EngageEd.repository.FolderRepository;
import com.EngageEd.EngageEd.service.FolderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final DepartmentChiefRepository departmentChiefRepository;

    @Autowired
    public FolderServiceImpl(FolderRepository folderRepository, DepartmentChiefRepository departmentChiefRepository) {
        this.folderRepository = folderRepository;
        this.departmentChiefRepository = departmentChiefRepository;
    }

    @Override
    public FolderResponseDTO createFolder(FolderRequestDTO requestDTO, Long departmentChiefId) {
        DepartmentChief chief = departmentChiefRepository.findById(departmentChiefId)
                .orElseThrow(() -> new RuntimeException("Department Chief not found with ID: " + departmentChiefId));

        Folder folder = new Folder();
        folder.setName(requestDTO.getName());
        folder.setCreatedBy(chief);
        folder.setCreatedAt(new Date());

        Folder savedFolder = folderRepository.save(folder);
        return FolderMapper.toDTO(savedFolder);
    }
    
    // Legacy method for backward compatibility
    @Override
    public Folder createFolder(String name, Long departmentChiefId) {
        FolderRequestDTO requestDTO = new FolderRequestDTO();
        requestDTO.setName(name);
        
        // Use the DTO version and convert back to entity
        createFolder(requestDTO, departmentChiefId);
        
        // Return the entity for backward compatibility
        return folderRepository.findByNameAndCreatedById(name, departmentChiefId)
                .orElseThrow(() -> new RuntimeException("Error retrieving newly created folder"));
    }

    @Override
    public FolderResponseDTO renameFolder(Long folderId, String newName) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found with ID: " + folderId));

        folder.setName(newName);
        Folder updatedFolder = folderRepository.save(folder);
        return FolderMapper.toDTO(updatedFolder);
    }

    @Override
    public List<FolderResponseDTO> getAllFolders() {
        List<Folder> folders = folderRepository.findAll();
        return folders.stream()
                .map(FolderMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // For backward compatibility
    @Override
    public List<Folder> getAllFoldersLegacy() {
        return folderRepository.findAll();
    }

    @Override
    public FolderResponseDTO getFolderById(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found with ID: " + folderId));
        return FolderMapper.toDTO(folder);
    }

    @Override
    public void deleteFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found with ID: " + folderId));

        folderRepository.delete(folder);
    }
}

