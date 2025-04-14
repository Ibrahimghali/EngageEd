package com.EngageEd.EngageEd.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {
    List<Subject> findByCreator(Professor professor);
    
    Optional<Subject> findBySubjectCode(String subjectCode);
    
    boolean existsBySubjectCode(String subjectCode);
    
    List<Subject> findByCreatorAndActive(Professor professor, boolean active);
    
    // Custom query to find subjects with search criteria
    List<Subject> findByNameContainingIgnoreCaseAndActive(String name, boolean active);

    // Changed from Optional to List
    List<Subject> findByActive(boolean active);
    
    long countByCreator(Professor professor);

    List<Subject> findByActiveTrue();
}