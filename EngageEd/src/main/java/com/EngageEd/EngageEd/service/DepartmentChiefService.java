package com.EngageEd.EngageEd.service;

import com.EngageEd.EngageEd.model.DepartmentChief;

public interface DepartmentChiefService extends UserService<DepartmentChief> {
    void createFolder(String folderName);
}