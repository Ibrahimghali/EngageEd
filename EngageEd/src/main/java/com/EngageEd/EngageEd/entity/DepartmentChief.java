package com.EngageEd.EngageEd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "department_chiefs")
@PrimaryKeyJoinColumn(name = "user_id")
public class DepartmentChief extends User {

    @Column(nullable = false, length = 100)
    private String department;
}


