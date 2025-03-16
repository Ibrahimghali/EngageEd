package com.EngageEd.EngageEd.dto;

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
@Table(name = "Subject")
public class Subject {

    @Id
    private Long id;
    private String name;
    private String code;
    private Long createdBy;

}
