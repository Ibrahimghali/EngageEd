package com.EngageEd.EngageEd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EngageEd.EngageEd.model.DepartmentChief;

public interface DepartmentChiefRepository extends JpaRepository<DepartmentChief, Long> {
    // Custom query methods can be defined here if needed
    // For example, you can add methods to find DepartmentChief by specific attributes

}
