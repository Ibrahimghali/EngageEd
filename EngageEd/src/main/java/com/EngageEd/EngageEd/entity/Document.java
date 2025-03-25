package com.EngageEd.EngageEd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "Document")
public class Document {
    @Id
    private Long id;
    private String name;
    private String fileUrl;
    private Long uploadedBy;  // ID du professeur qui a uploadé le document
    private Long folderId;

}
