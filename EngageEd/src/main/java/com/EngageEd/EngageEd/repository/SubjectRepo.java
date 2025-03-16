package com.EngageEd.EngageEd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.dto.Subject;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Long> {
    List<SubjectRepo> findByCreatedBy(Long departmentChiefId);
}
