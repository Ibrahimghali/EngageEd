package com.EngageEd.EngageEd.controller;

import com.EngageEd.EngageEd.model.Document;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping("/{professorId}")
    public ResponseEntity<Professor> getProfessor(@PathVariable Long professorId) {
        Professor professor = professorService.getProfessorById(professorId);
        return ResponseEntity.ok(professor);
    }

    @GetMapping("/{professorId}/documents")
    public ResponseEntity<List<Document>> getProfessorDocuments(@PathVariable Long professorId) {
        List<Document> documents = professorService.getProfessorDocuments(professorId);
        return ResponseEntity.ok(documents);
    }

    @DeleteMapping("/{professorId}/documents/{documentId}")
    public ResponseEntity<Void> deleteProfessorDocument(
            @PathVariable Long professorId,
            @PathVariable Long documentId) {
        professorService.deleteProfessorDocument(professorId, documentId);
        return ResponseEntity.noContent().build();
    }
}