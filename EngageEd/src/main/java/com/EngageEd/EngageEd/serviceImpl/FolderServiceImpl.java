package com.EngageEd.EngageEd.serviceImpl;

import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Folder;
import com.EngageEd.EngageEd.repository.DepartmentChiefRepository;
import com.EngageEd.EngageEd.repository.FolderRepository;
import com.EngageEd.EngageEd.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final DepartmentChiefRepository departmentChiefRepository;

    @Autowired
    public FolderServiceImpl(FolderRepository folderRepository, DepartmentChiefRepository departmentChiefRepository) {
        this.folderRepository = folderRepository;
        this.departmentChiefRepository = departmentChiefRepository;
    }

    @Override
    public Folder createFolder(String name, Long departmentChiefId) {
        DepartmentChief chief = departmentChiefRepository.findById(departmentChiefId)
                .orElseThrow(() -> new RuntimeException("Department Chief not found"));

        Folder folder = new Folder();
        folder.setName(name);
        folder.setCreatedBy(chief);

        return folderRepository.save(folder);
    }

    @Override
    public Folder renameFolder(Long folderId, String newName) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        folder.setName(newName);
        return folderRepository.save(folder);
    }

    @Override
    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    @Override
    public void deleteFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        folderRepository.delete(folder);
    }
}

