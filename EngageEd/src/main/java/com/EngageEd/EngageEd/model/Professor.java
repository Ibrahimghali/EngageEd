package com.EngageEd.EngageEd.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professors")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder // Add this annotation
public class Professor extends User {
    @Column
    private String specialization;
    
    @ManyToOne
    @JoinColumn(name = "registered_by_id")
    private DepartmentChief registeredBy;
    
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private Set<Subject> createdSubjects = new HashSet<>();
}