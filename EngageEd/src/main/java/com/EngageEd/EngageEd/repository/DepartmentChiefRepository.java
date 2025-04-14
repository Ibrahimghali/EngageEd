package com.EngageEd.EngageEd.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.model.DepartmentChief;

@Repository
public interface DepartmentChiefRepository extends JpaRepository<DepartmentChief, UUID> {
    Optional<DepartmentChief> findByEmail(String email);
    
    Optional<DepartmentChief> findByFirebaseUid(String firebaseUid);
}