package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.ProfessorDTOs;
import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.dto.AuthenticationDTOs;

public interface ProfessorService {

    Professor createProfessor(ProfessorDTOs.ProfessorRegistrationRequest request, DepartmentChief registeredBy);

    ProfessorDTOs.ProfessorResponse registerProfessor(ProfessorDTOs.ProfessorRegistrationRequest request);

    ProfessorDTOs.ProfessorResponse inviteProfessor(
            ProfessorDTOs.ProfessorInviteRequest request, 
            DepartmentChief departmentChief);

    ProfessorDTOs.ProfessorResponse getProfessorById(UUID id);

    ProfessorDTOs.ProfessorResponse getProfessorByEmail(String email);

    ProfessorDTOs.ProfessorResponse getProfessorByFirebaseUid(String firebaseUid);

    Professor getProfessorEntityById(UUID id);

    List<ProfessorDTOs.ProfessorResponse> getAllProfessors();

    List<ProfessorDTOs.ProfessorResponse> getProfessorsByDepartmentChief(UUID departmentChiefId);

    PageResponse<ProfessorDTOs.ProfessorResponse> getProfessorsPaged(int page, int size);

    ProfessorDTOs.ProfessorResponse updateProfessor(UUID id, ProfessorDTOs.ProfessorUpdateRequest request);

    void deleteProfessor(UUID id);

    Professor createProfessor(ProfessorDTOs.ProfessorRegistrationRequest request);

    @Transactional(readOnly = true)
    Professor findProfessorByEmail(String email);

    Professor findProfessorEntityByEmail(String email);

    Professor createProfessor(AuthenticationDTOs.RegistrationRequest request, String firebaseUid);
}
