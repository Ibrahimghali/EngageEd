package com.EngageEd.EngageEd.dto;

import jakarta.persistence.Entity;
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

    private Long id;
    private String name;
    private String code;
    private Long createdBy;

}
