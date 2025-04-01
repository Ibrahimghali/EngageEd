package com.EngageEd.EngageEd.controller;

import com.EngageEd.EngageEd.model.Document;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/{studentId}/documents")
    public ResponseEntity<List<Document>> getAccessibleDocuments(@PathVariable Long studentId) {
        List<Document> documents = studentService.getAllAccessibleDocuments(studentId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{studentId}/documents/{documentId}")
    public ResponseEntity<Document> getDocument(
            @PathVariable Long studentId,
            @PathVariable Long documentId) {
        Document document = studentService.getDocumentById(studentId, documentId);
        return ResponseEntity.ok(document);
    }
}