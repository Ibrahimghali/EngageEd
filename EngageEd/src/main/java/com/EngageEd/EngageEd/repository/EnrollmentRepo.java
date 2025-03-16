package com.EngageEd.EngageEd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.dto.Enrollment;

@Repository
public interface EnrollmentRepo extends JpaRepository<EnrollmentRepo, Long> {
    List<Enrollment> findByUserId(Long userId);
    List<Enrollment> findBySubjectId(Long subjectId);
    EnrollmentRepo findByUserIdAndSubjectId(Long userId, Long subjectId);
}
