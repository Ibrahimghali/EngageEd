package com.EngageEd.EngageEd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "professors")
@Data
public class Professor extends User {
    // Can add extra attributes later
}
