package com.EngageEd.EngageEd.model;

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
@Table(name = "students")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder // Add this annotation
@NoArgsConstructor
public class Student extends User {
    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments = new HashSet<>();
}