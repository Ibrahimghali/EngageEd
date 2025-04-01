package com.EngageEd.EngageEd.service;

import java.util.List;

import com.EngageEd.EngageEd.model.Document;
import com.EngageEd.EngageEd.model.Student;

public interface StudentService {
    Student getStudentById(Long id);
    List<Document> getAllAccessibleDocuments(Long studentId);
    Document getDocumentById(Long studentId, Long documentId);
}

