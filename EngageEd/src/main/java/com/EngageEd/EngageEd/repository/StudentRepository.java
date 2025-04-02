package com.EngageEd.EngageEd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EngageEd.EngageEd.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
