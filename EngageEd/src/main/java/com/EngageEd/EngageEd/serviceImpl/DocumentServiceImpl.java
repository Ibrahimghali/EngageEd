package com.EngageEd.EngageEd.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.EngageEd.EngageEd.dto.document.DocumentRequestDTO;
import com.EngageEd.EngageEd.dto.document.DocumentResponseDTO;
import com.EngageEd.EngageEd.dto.mapper.DocumentMapper;
import com.EngageEd.EngageEd.model.Document;
import com.EngageEd.EngageEd.model.Folder;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.repository.DocumentRepository;
import com.EngageEd.EngageEd.repository.FolderRepository;
import com.EngageEd.EngageEd.repository.ProfessorRepository;
import com.EngageEd.EngageEd.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final DocumentRepository documentRepository;
    private final ProfessorRepository professorRepository;
    private final FolderRepository folderRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository, 
                              ProfessorRepository professorRepository, 
                              FolderRepository folderRepository) {
        this.documentRepository = documentRepository;
        this.professorRepository = professorRepository;
        this.folderRepository = folderRepository;
    }

    @Override
    public DocumentResponseDTO uploadDocument(DocumentRequestDTO dto, MultipartFile file, Long professorId) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty. Please upload a valid document.");
        }

        try {
            Professor professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new RuntimeException("Professor not found with ID: " + professorId));
            Folder folder = folderRepository.findById(dto.getFolderId())
                    .orElseThrow(() -> new RuntimeException("Folder not found with ID: " + dto.getFolderId()));

            // Ensure upload directory exists
            Path uploadPath = Path.of(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Create document entity
            Document document = new Document();
            document.setTitle(dto.getTitle());
            document.setDescription(dto.getDescription());
            document.setType(dto.getType());
            document.setFilePath(filePath.toString());
            document.setUploadedBy(professor);
            document.setFolder(folder);
            document.setUploadedAt(new Date());

            Document savedDocument = documentRepository.save(document);
            return DocumentMapper.toDTO(savedDocument);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Error: " + e.getMessage());
        }
    }

    // Keep old method for backward compatibility
    @Override
    public Document uploadDocument(String title, String description, String type, 
                                 MultipartFile file, Long professorId, Long folderId) {
        DocumentRequestDTO dto = new DocumentRequestDTO();
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setType(type);
        dto.setFolderId(folderId);
        
        // Set a temporary path - it will be overwritten in the method
        dto.setFilePath("temp");
        
        DocumentResponseDTO responseDTO = uploadDocument(dto, file, professorId);
        return documentRepository.findById(responseDTO.getId())
            .orElseThrow(() -> new RuntimeException("Error retrieving saved document"));
    }

    @Override
    public DocumentResponseDTO renameDocument(Long documentId, String newTitle) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

        document.setTitle(newTitle);
        Document savedDocument = documentRepository.save(document);
        return DocumentMapper.toDTO(savedDocument);
    }

    @Override
    public DocumentResponseDTO findById(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));
        return DocumentMapper.toDTO(document);
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
    public List<DocumentResponseDTO> getAllDocumentsByFolder(Long folderId) {
        List<Document> documents = documentRepository.findByFolderId(folderId);
        return documents.stream()
                .map(DocumentMapper::toDTO)
                .collect(Collectors.toList());
    }
}