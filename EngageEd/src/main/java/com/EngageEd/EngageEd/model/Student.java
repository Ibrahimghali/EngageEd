package com.EngageEd.EngageEd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "students")
@Data
public class Student extends User {
    // Can add extra attributes later
}

