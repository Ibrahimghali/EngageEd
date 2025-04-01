package com.EngageEd.EngageEd.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String type;  // e.g., PDF, DOCX, etc.

    @Column(nullable = false)
    private String filePath;  // Path to file storage

    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = false)
    private Professor uploadedBy;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;
}

