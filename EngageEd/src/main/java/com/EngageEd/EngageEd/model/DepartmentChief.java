package com.EngageEd.EngageEd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "department_chiefs")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DepartmentChief extends User {
    @Column(nullable = false)
    private String departmentName;
    
    @OneToMany(mappedBy = "registeredBy")
    private Set<Professor> managedProfessors = new HashSet<>();
}