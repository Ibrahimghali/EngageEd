package com.EngageEd.EngageEd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.entity.Document;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {
    List<Document> findByFolderId(Long folderId);
    List<DocumentRepo> findByUploadedBy(Long professorId);
}