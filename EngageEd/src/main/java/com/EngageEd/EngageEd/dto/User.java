package com.EngageEd.EngageEd.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "User")
public class User {
    
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

}
