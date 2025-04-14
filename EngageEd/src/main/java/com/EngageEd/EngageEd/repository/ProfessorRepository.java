package com.EngageEd.EngageEd.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
    Optional<Professor> findByEmail(String email);
    
    Optional<Professor> findByFirebaseUid(String firebaseUid);
    
    List<Professor> findByRegisteredBy(DepartmentChief departmentChief);
    
    long countByRegisteredBy(DepartmentChief departmentChief);

    // Changed from Optional to List
    List<Professor> findByRegisteredById(UUID departmentChiefId);

    boolean existsByEmail(String email);
}