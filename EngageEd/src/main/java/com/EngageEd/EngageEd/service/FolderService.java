package com.EngageEd.EngageEd.service;


import com.EngageEd.EngageEd.model.Folder;
import java.util.List;

public interface FolderService {
    Folder createFolder(String name, Long departmentChiefId);
    Folder renameFolder(Long folderId, String newName);
    List<Folder> getAllFolders();
    void deleteFolder(Long folderId);
}

