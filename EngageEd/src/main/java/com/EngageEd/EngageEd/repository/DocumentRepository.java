package com.EngageEd.EngageEd.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    // Custom query methods can be defined here if needed
    // For example, you can add methods to find documents by specific attributes
    
    List<Document> findByTitle(String title);
    List<Document> findByDescription(String description);
    List<Document> findByType(String type);
    List<Document> findDocumentsById(Long id);
    List<Document> findByFolderId(Long folderId);


}