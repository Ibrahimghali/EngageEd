package com.EngageEd.EngageEd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)  // Using JOINED strategy
public abstract class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @Column(nullable = false, length = 50)
    protected String firstName;

    @Column(nullable = false, length = 50)
    protected String lastName;

    @Column(nullable = false, unique = true, length = 100)
    protected String email;

    @Column(nullable = false)
    protected String password;  // Store hashed password

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;

    
}
