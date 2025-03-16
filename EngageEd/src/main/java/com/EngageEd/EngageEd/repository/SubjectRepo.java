package com.EngageEd.EngageEd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepo extends JpaRepository<SubjectRepo, Long> {
    List<SubjectRepo> findByCreatedBy(Long departmentChiefId);
}
