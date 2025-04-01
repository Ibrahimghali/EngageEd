package com.EngageEd.EngageEd.serviceImpl;


import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Folder;
import com.EngageEd.EngageEd.repository.DepartmentChiefRepository;
import com.EngageEd.EngageEd.repository.FolderRepository;
import com.EngageEd.EngageEd.service.DepartmentChiefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentChiefServiceImpl implements DepartmentChiefService {

    private final DepartmentChiefRepository departmentChiefRepository;
    private final FolderRepository folderRepository;

    @Autowired
    public DepartmentChiefServiceImpl(DepartmentChiefRepository departmentChiefRepository,
                                    FolderRepository folderRepository) {
        this.departmentChiefRepository = departmentChiefRepository;
        this.folderRepository = folderRepository;
    }

    @Override
    public DepartmentChief getDepartmentChiefById(Long id) {
        return departmentChiefRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department Chief not found"));
    }

    @Override
    public List<Folder> getFoldersCreatedBy(Long departmentChiefId) {
        return folderRepository.findByCreatedById(departmentChiefId);
    }

    @Override
    public void deleteFolder(Long departmentChiefId, Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!folder.getCreatedBy().getId().equals(departmentChiefId)) {
            throw new RuntimeException("You can only delete folders you created");
        }

        folderRepository.delete(folder);
    }
}