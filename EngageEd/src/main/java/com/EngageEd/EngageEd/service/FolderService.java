package com.EngageEd.EngageEd.service;

import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.dto.Folder;
import com.EngageEd.EngageEd.repository.FolderRepo;

@Service
public class FolderService {
    private final FolderRepo folderRepository;

    // @Autowired
    public FolderService(FolderRepo folderRepository) {
        this.folderRepository = folderRepository;
    }

    public List<Folder> getFoldersBySubject(Long subjectId) {
        return folderRepository.findBySubjectId(subjectId);
    }

    public Folder createFolder(Folder folder) {
        return folderRepository.save(folder);
    }
}

