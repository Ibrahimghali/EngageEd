package com.EngageEd.EngageEd.service;

import java.util.List;

import com.EngageEd.EngageEd.model.*;

public interface ProfessorService {
    Professor getProfessorById(Long id);
    List<Document> getProfessorDocuments(Long professorId);
    void deleteProfessorDocument(Long professorId, Long documentId);
}

