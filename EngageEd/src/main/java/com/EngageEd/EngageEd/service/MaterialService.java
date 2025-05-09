package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.EngageEd.EngageEd.dto.MaterialDTOs;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.model.Material;
import com.EngageEd.EngageEd.model.MaterialType;
import com.EngageEd.EngageEd.model.Professor;

public interface MaterialService {
        
        MaterialDTOs.MaterialResponse uploadMaterial(
                        MaterialDTOs.MaterialUploadRequest request, 
                        MultipartFile file, 
                        Professor professor);
        
        MaterialDTOs.MaterialResponse getMaterialById(UUID id);
        
        Material getMaterialEntityById(UUID id);
        
        List<MaterialDTOs.MaterialResponse> getMaterialsBySubject(UUID subjectId);
        
        List<MaterialDTOs.MaterialResponse> getMaterialsBySubjectAndType(UUID subjectId, MaterialType type);
        
        List<MaterialDTOs.MaterialResponse> searchMaterialsInSubject(UUID subjectId, String title);
        
        PageResponse<MaterialDTOs.MaterialResponse> getMaterialsBySubjectPaged(UUID subjectId, int page, int size);
        
        MaterialDTOs.MaterialResponse updateMaterial(
                        UUID id, 
                        MaterialDTOs.MaterialUpdateRequest request, 
                        Professor professor);
        
        MaterialDTOs.MaterialResponse replaceMaterialFile(UUID id, MultipartFile file, Professor professor);
        
        void deleteMaterial(UUID id, Professor professor);

        MaterialDTOs.MaterialResponse createMaterial(MaterialDTOs.MaterialCreationRequest request, UUID subjectId, Professor professor);
}
