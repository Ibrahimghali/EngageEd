package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.UUID;

import com.EngageEd.EngageEd.dto.AuthenticationDTOs;
import com.EngageEd.EngageEd.dto.DepartmentChiefDTOs;
import com.EngageEd.EngageEd.dto.DepartmentChiefDTOs.DepartmentChiefRegistrationRequest;
import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.model.DepartmentChief;

import jakarta.validation.Valid;

public interface DepartmentChiefService {
    
    DepartmentChief createDepartmentChief(DepartmentChiefDTOs.DepartmentChiefRegistrationRequest request);
    
    DepartmentChief createDepartmentChief(AuthenticationDTOs.RegistrationRequest request, String firebaseUid);

    DepartmentChiefDTOs.DepartmentChiefResponse getDepartmentChiefById(UUID id);
    
    DepartmentChief getDepartmentChiefEntityById(UUID id);
    
    DepartmentChiefDTOs.DepartmentChiefResponse getDepartmentChiefByEmail(String email);
    
    DepartmentChiefDTOs.DepartmentChiefResponse getDepartmentChiefByFirebaseUid(String firebaseUid);

    List<DepartmentChiefDTOs.DepartmentChiefResponse> getAllDepartmentChiefs();
    
    PageResponse<DepartmentChiefDTOs.DepartmentChiefResponse> getDepartmentChiefsPaged(int page, int size);

    DepartmentChiefDTOs.DepartmentChiefResponse updateDepartmentChief(UUID id, DepartmentChiefDTOs.DepartmentChiefUpdateRequest request);

    void deleteDepartmentChief(UUID id);
    
    DepartmentChief findDepartmentChiefEntityByEmail(String email);

    DepartmentChief createDepartmentChiefWithFirebaseUid(
            DepartmentChiefRegistrationRequest request, String firebaseUid);
}
