package com.EngageEd.EngageEd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Folder")
public class Folder {

    @Id
    private Long id;
    private String name;
    private String description;
    private Long subjectId;

}
