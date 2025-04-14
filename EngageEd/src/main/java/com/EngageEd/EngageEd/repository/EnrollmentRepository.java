package com.EngageEd.EngageEd.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.model.Enrollment;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.model.Subject;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    List<Enrollment> findByStudent(Student student);
    
    List<Enrollment> findByStudentAndActive(Student student, boolean active);
    
    List<Enrollment> findBySubject(Subject subject);
    
    List<Enrollment> findBySubjectAndActive(Subject subject, boolean active);
    
    Optional<Enrollment> findByStudentAndSubject(Student student, Subject subject);
    
    boolean existsByStudentAndSubject(Student student, Subject subject);
    
    long countBySubject(Subject subject);

    long countByStudent(Student student);

    Page<Enrollment> findBySubject(Subject subject, PageRequest of);

    boolean existsByStudentAndSubjectAndActive(Student student, Subject subject, boolean b);

    long countByStudentAndActive(Student student, boolean b);

    long countBySubjectAndActive(Subject subject, boolean b);

    Optional<Subject> findByStudentId(UUID id);

    Set<UUID> findSubjectIdsByStudentId(UUID id);
}