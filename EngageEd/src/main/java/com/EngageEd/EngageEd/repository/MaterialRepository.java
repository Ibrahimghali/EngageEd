package com.EngageEd.EngageEd.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.EngageEd.EngageEd.model.Material;
import com.EngageEd.EngageEd.model.MaterialType;
import com.EngageEd.EngageEd.model.Subject;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID>, JpaSpecificationExecutor<Material> {
    List<Material> findBySubject(Subject subject);
    
    List<Material> findBySubjectAndType(Subject subject, MaterialType type);
    
    // Find materials with sorting by uploaded date
    List<Material> findBySubjectOrderByUploadedAtDesc(Subject subject);
    
    // Search materials by title
    List<Material> findBySubjectAndTitleContainingIgnoreCase(Subject subject, String title);
    
    // Count materials by subject and type
    long countBySubjectAndType(Subject subject, MaterialType type);
    
    // Added missing method to count all materials for a subject
    long countBySubject(Subject subject);

    List<Material> findBySubjectId(UUID subjectId);

    

}