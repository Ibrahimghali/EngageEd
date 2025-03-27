package com.EngageEd.EngageEd.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professors")
@PrimaryKeyJoinColumn(name = "user_id")
public class Professor extends User {

    @Column(nullable = false, length = 100)
    private String department;

    @ElementCollection(targetClass = ProfessorType.class)
    @Enumerated(EnumType.STRING)  // Store as string in DB
    @CollectionTable(name = "professor_types", joinColumns = @JoinColumn(name = "professor_id"))
    @Column(name = "type")
    private Set<ProfessorType> types = new HashSet<>();

    // Add a method to check if a professor has a certain type
    public boolean hasType(ProfessorType type) {
        return types.contains(type);
    }
}
