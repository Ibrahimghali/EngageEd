package com.EngageEd.EngageEd.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "students")
@Data
public class Student extends User {
    // Can add extra attributes later
}

