package com.EngageEd.EngageEd.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Enrollment")
public class Enrollment {

    private Long id;
    private Long userId;     // ID de l'étudiant inscrit
    private Long subjectId;  // ID de la matière concernée
    private String invitationCode;
}
