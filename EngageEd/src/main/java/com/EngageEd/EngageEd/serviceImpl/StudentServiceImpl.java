package com.EngageEd.EngageEd.serviceImpl;

import com.EngageEd.EngageEd.model.Document;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.repository.DocumentRepository;
import com.EngageEd.EngageEd.repository.StudentRepository;
import com.EngageEd.EngageEd.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                            DocumentRepository documentRepository) {
        this.studentRepository = studentRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public List<Document> getAllAccessibleDocuments(Long studentId) {
        // Verify student exists
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }
        // Return all documents (in a real system, you might filter by access rights)
        return documentRepository.findAll();
    }

    @Override
    public Document getDocumentById(Long studentId, Long documentId) {
        // Verify student exists
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }
        
        // Get the document (no special access control in this basic version)
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }
}