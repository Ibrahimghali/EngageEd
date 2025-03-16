package com.EngageEd.EngageEd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.dto.Folder;


@Repository
public interface FolderRepo extends JpaRepository<FolderRepo, Long> {
    List<Folder> findBySubjectId(Long subjectId);
}
