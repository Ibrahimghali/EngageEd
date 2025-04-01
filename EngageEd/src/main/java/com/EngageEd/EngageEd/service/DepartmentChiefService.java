package com.EngageEd.EngageEd.service;


import java.util.List;

import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Folder;

public interface DepartmentChiefService {
    DepartmentChief getDepartmentChiefById(Long id);
    List<Folder> getFoldersCreatedBy(Long departmentChiefId);
    void deleteFolder(Long departmentChiefId, Long folderId);
}