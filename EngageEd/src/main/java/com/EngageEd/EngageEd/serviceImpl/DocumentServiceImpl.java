package com.EngageEd.EngageEd.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.EngageEd.EngageEd.model.Document;
import com.EngageEd.EngageEd.model.Folder;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.repository.DocumentRepository;
import com.EngageEd.EngageEd.repository.FolderRepository;
import com.EngageEd.EngageEd.repository.ProfessorRepository;
import com.EngageEd.EngageEd.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ProfessorRepository professorRepository;
    private final FolderRepository folderRepository;
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, 
                              ProfessorRepository professorRepository, 
                              FolderRepository folderRepository) {
        this.documentRepository = documentRepository;
        this.professorRepository = professorRepository;
        this.folderRepository = folderRepository;
    }

    @Override
    public Document uploadDocument(String title, String description, String type, 
                                 MultipartFile file, Long professorId, Long folderId) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty. Please upload a valid document.");
        }

        try {
            Professor professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new RuntimeException("Professor not found with ID: " + professorId));
            Folder folder = folderRepository.findById(folderId)
                    .orElseThrow(() -> new RuntimeException("Folder not found with ID: " + folderId));

            // Ensure upload directory exists
            Path uploadPath = Path.of(uploadDir);  // Use the injected uploadDir
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Save document metadata
            Document document = new Document();
            document.setTitle(title);
            document.setDescription(description);
            document.setType(type);
            document.setFilePath(filePath.toString());
            document.setUploadedBy(professor);
            document.setFolder(folder);

            return documentRepository.save(document);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file: " + file.getOriginalFilename() + 
                                     ". Error: " + e.getMessage());
        }
    }

    @Override
    public Document renameDocument(Long documentId, String newTitle) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

        document.setTitle(newTitle);
        return documentRepository.save(document);
    }

    @Override
    public byte[] downloadDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

        Path filePath = Path.of(document.getFilePath());
        try {
            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found on server: " + filePath);
            }
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage());
        }
    }

    @Override
    public void deleteDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

        Path filePath = Path.of(document.getFilePath());
        try {
            Files.deleteIfExists(filePath);
            documentRepository.delete(document);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file: " + filePath + ". Error: " + e.getMessage());
        }
    }

    @Override
    public List<Document> getAllDocumentsByFolder(Long folderId) {
        return documentRepository.findByFolderId(folderId);
    }
}