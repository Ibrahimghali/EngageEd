package com.EngageEd.EngageEd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)  // Normalized structure
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements Serializable{  // Abstract to prevent direct instantiation

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // Étudiant, Professeur, Chef de Département
}
