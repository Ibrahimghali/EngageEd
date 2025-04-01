package com.EngageEd.EngageEd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EngageEd.EngageEd.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // Additional query methods can be defined here if needed
    // For example, you can add methods to find students by specific criteria
    // List<Student> findBySomeField(String someField);

}
