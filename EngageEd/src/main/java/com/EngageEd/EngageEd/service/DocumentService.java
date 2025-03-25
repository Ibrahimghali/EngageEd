package com.EngageEd.EngageEd.service;

import com.EngageEd.EngageEd.entity.Document;
import java.util.List;

public interface DocumentService {

    // here are list of all methode that we will use in our project
    // we will implement these methodes in our DocumentServiceImpl class
    // we will use these methodes in our DocumentController class
    // we will use these methodes in our DocumentRepository class
    
    // methode to save a document
    public void saveDocument(Document document);
    // methode to get a document by id
    public Document getDocumentById(int id);
    // methode to get all documents
    public List<Document> getAllDocuments();
    // methode to delete a document
    public void deleteDocument(int id);
    // methode to get all documents by folder
    public List<Document> getDocumentsByFolder(int folderId);
    // methode to get all documents by user
    public List<Document> getDocumentsByUser(int userId);
    

}
