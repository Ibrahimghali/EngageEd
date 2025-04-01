package com.EngageEd.EngageEd.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "department_chiefs")
@Data
// Removed conflicting annotations to avoid duplicate constructors
public class DepartmentChief extends Professor {
    // Can add extra attributes later (like permissions)
}



