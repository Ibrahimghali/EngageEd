package com.EngageEd.EngageEd.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String code;
    private String description;
    private Long departmentId;  // ID du département auquel appartient la matière
    private Long professorId;  // ID du professeur qui enseigne la matière
    private Long chiefId;  // ID du chef de département qui gère la matière
    private boolean iswithTD;  // Si la matière a des TD
    private boolean iswithTP;  // Si la matière a des TP
    

}
