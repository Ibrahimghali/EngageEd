package com.EngageEd.EngageEd.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.model.Document;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.repository.DocumentRepository;
import com.EngageEd.EngageEd.repository.ProfessorRepository;
import com.EngageEd.EngageEd.service.ProfessorService;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public ProfessorServiceImpl(ProfessorRepository professorRepository, DocumentRepository documentRepository) {
        this.professorRepository = professorRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public Professor getProfessorById(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor not found"));
    }

    @Override
    public List<Document> getProfessorDocuments(Long professorId) {
        return documentRepository.findByUploadedById(professorId);
    }

    @Override
    public void deleteProfessorDocument(Long professorId, Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!document.getUploadedBy().getId().equals(professorId)) {
            throw new RuntimeException("You can only delete your own documents.");
        }

        documentRepository.delete(document);
    }
}