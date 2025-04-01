package com.EngageEd.EngageEd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.model.Folder;


@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByCreatedById(Long departmentChiefId);
}
