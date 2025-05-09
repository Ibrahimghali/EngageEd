package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.UUID;

import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.SubjectDTOs;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Subject;

public interface SubjectService {

    Subject createSubject(SubjectDTOs.SubjectCreationRequest request, Professor professor);

    SubjectDTOs.SubjectResponse createAndReturnSubject(SubjectDTOs.SubjectCreationRequest request, Professor professor);

    SubjectDTOs.SubjectResponse getSubjectById(UUID id);

    Subject getSubjectEntityById(UUID id);

    SubjectDTOs.SubjectResponse getSubjectByCode(String subjectCode);

    Subject getSubjectEntityByCode(String subjectCode);

    List<SubjectDTOs.SubjectResponse> getAllSubjects();

    List<SubjectDTOs.SubjectResponse> getActiveSubjects();

    List<SubjectDTOs.SubjectResponse> getSubjectsByProfessor(Professor professor);

    List<SubjectDTOs.SubjectResponse> getSubjectsByProfessor(UUID professorId);

    List<SubjectDTOs.SubjectResponse> getActiveSubjectsByProfessor(Professor professor);

    List<SubjectDTOs.SubjectResponse> getActiveSubjectsByProfessor(UUID professorId);

    List<SubjectDTOs.SubjectResponse> searchSubjects(String name);

    PageResponse<SubjectDTOs.SubjectResponse> getSubjectsPaged(int page, int size);

    SubjectDTOs.SubjectResponse updateSubject(
            UUID id, 
            SubjectDTOs.SubjectUpdateRequest request, 
            Professor professor);

    void deleteSubject(UUID id, Professor professor);

    String generateUniqueSubjectCode();

    List<SubjectDTOs.SubjectResponse> getAvailableSubjectsForStudent(String studentEmail);
}
