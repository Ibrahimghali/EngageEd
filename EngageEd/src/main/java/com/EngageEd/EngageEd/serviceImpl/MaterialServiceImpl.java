package com.EngageEd.EngageEd.serviceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.EngageEd.EngageEd.dto.MaterialDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.exception.AccessDeniedException;
import com.EngageEd.EngageEd.exception.FileStorageException;
import com.EngageEd.EngageEd.exception.ResourceNotFoundException;
import com.EngageEd.EngageEd.model.Material;
import com.EngageEd.EngageEd.model.MaterialType;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Subject;
import com.EngageEd.EngageEd.repository.MaterialRepository;
import com.EngageEd.EngageEd.service.FileStorageService;
import com.EngageEd.EngageEd.service.MaterialService;
import com.EngageEd.EngageEd.service.SubjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final SubjectService subjectService;
    private final FileStorageService fileStorageService;
    
    @Override
    @Transactional
    public MaterialDTOs.MaterialResponse uploadMaterial(MaterialDTOs.MaterialUploadRequest request, 
            MultipartFile file, Professor professor) {
        log.info("Uploading material: {} for subject ID: {} by professor ID: {}", 
                request.getTitle(), request.getSubjectId(), professor.getId());
        
        Subject subject = subjectService.getSubjectEntityById(request.getSubjectId());
        
        // Verify that the professor is the creator of the subject
        if (!subject.getCreator().getId().equals(professor.getId())) {
            throw new AccessDeniedException("You do not have permission to upload materials to this subject");
        }
        
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        long fileSize = file.getSize();
        
        // Store the file and get the URL
        String fileUrl;
        try {
            fileUrl = fileStorageService.storeFile(file, subject.getSubjectCode());
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file: " + e.getMessage());
        }
        
        Material material = Material.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .fileUrl(fileUrl)
                .fileName(fileName)
                .fileSize(fileSize)
                .contentType(contentType)
                .subject(subject)
                .uploadedAt(LocalDateTime.now())
                .build();
        
        material = materialRepository.save(material);
        
        return toMaterialResponse(material);
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialDTOs.MaterialResponse getMaterialById(UUID id) {
        log.info("Fetching material with ID: {}", id);
        return toMaterialResponse(getMaterialEntityById(id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Material getMaterialEntityById(UUID id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTOs.MaterialResponse> getMaterialsBySubject(UUID subjectId) {
        log.info("Fetching materials for subject ID: {}", subjectId);
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        
        return materialRepository.findBySubjectOrderByUploadedAtDesc(subject).stream()
                .map(this::toMaterialResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTOs.MaterialResponse> getMaterialsBySubjectAndType(UUID subjectId, MaterialType type) {
        log.info("Fetching materials for subject ID: {} and type: {}", subjectId, type);
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        
        return materialRepository.findBySubjectAndType(subject, type).stream()
                .map(this::toMaterialResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTOs.MaterialResponse> searchMaterialsInSubject(UUID subjectId, String title) {
        log.info("Searching materials with title containing: {} in subject ID: {}", title, subjectId);
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        
        return materialRepository.findBySubjectAndTitleContainingIgnoreCase(subject, title).stream()
                .map(this::toMaterialResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResponse<MaterialDTOs.MaterialResponse> getMaterialsBySubjectPaged(UUID subjectId, int page, int size) {
        log.info("Fetching paged materials for subject ID: {}, page: {}, size: {}", subjectId, page, size);
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        
        Page<Material> materialPage = materialRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("subject"), subject), 
                PageRequest.of(page, size));
        
        List<MaterialDTOs.MaterialResponse> content = materialPage.getContent().stream()
                .map(this::toMaterialResponse)
                .collect(Collectors.toList());
        
        return PageResponse.<MaterialDTOs.MaterialResponse>of(
                content, 
                page, 
                size, 
                materialPage.getTotalElements());
    }

    @Override
    @Transactional
    public MaterialDTOs.MaterialResponse updateMaterial(UUID id, MaterialDTOs.MaterialUpdateRequest request, 
            Professor professor) {
        log.info("Updating material with ID: {} by professor ID: {}", id, professor.getId());
        
        Material material = getMaterialEntityById(id);
        
        // Verify that the professor is the creator of the subject
        if (!material.getSubject().getCreator().getId().equals(professor.getId())) {
            throw new AccessDeniedException("You do not have permission to update this material");
        }
        
        if (request.getTitle() != null) {
            material.setTitle(request.getTitle());
        }
        
        if (request.getDescription() != null) {
            material.setDescription(request.getDescription());
        }
        
        if (request.getType() != null) {
            material.setType(request.getType());
        }
        
        materialRepository.save(material);
        
        return toMaterialResponse(material);
    }
    
    @Override
    @Transactional
    public MaterialDTOs.MaterialResponse replaceMaterialFile(UUID id, MultipartFile file, Professor professor) {
        log.info("Replacing file for material ID: {} by professor ID: {}", id, professor.getId());
        
        Material material = getMaterialEntityById(id);
        
        // Verify that the professor is the creator of the subject
        if (!material.getSubject().getCreator().getId().equals(professor.getId())) {
            throw new AccessDeniedException("You do not have permission to replace this material's file");
        }
        
        // Delete the old file if it exists
        try {
            fileStorageService.deleteFile(material.getFileUrl());
        } catch (IOException e) {
            log.error("Failed to delete old file: {}", e.getMessage());
            // Continue even if old file deletion fails
        }
        
        // Store the new file
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        long fileSize = file.getSize();
        
        String fileUrl;
        try {
            fileUrl = fileStorageService.storeFile(file, material.getSubject().getSubjectCode());
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file: " + e.getMessage());
        }
        
        material.setFileName(fileName);
        material.setContentType(contentType);
        material.setFileSize(fileSize);
        material.setFileUrl(fileUrl);
        material.setUploadedAt(LocalDateTime.now());
        
        materialRepository.save(material);
        
        return toMaterialResponse(material);
    }

    @Override
    @Transactional
    public void deleteMaterial(UUID id, Professor professor) {
        log.info("Deleting material with ID: {} by professor ID: {}", id, professor.getId());
        
        Material material = getMaterialEntityById(id);
        
        // Verify that the professor is the creator of the subject
        if (!material.getSubject().getCreator().getId().equals(professor.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this material");
        }
        
        // Delete the file if it exists
        try {
            fileStorageService.deleteFile(material.getFileUrl());
        } catch (IOException e) {
            log.error("Failed to delete file: {}", e.getMessage());
            // Continue with deletion even if file couldn't be deleted
        }
        
        materialRepository.delete(material);
    }
    
    private MaterialDTOs.MaterialResponse toMaterialResponse(Material material) {
        return MaterialDTOs.MaterialResponse.builder()
                .id(material.getId())
                .title(material.getTitle())
                .description(material.getDescription())
                .type(material.getType())
                .fileUrl(material.getFileUrl())
                .fileName(material.getFileName())
                .fileSize(material.getFileSize())
                .contentType(material.getContentType())
                .subjectId(material.getSubject().getId())
                .subjectName(material.getSubject().getName())
                .uploadedAt(material.getUploadedAt())
                .build();
    }
}