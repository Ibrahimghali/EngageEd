package com.EngageEd.EngageEd.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "department_chiefs")
@Data
public class DepartmentChief extends Professor {
}



