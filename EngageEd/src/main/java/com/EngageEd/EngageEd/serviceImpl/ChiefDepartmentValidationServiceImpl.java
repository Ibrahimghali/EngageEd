package com.EngageEd.EngageEd.serviceImpl;

import java.util.Map;
import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.service.ChiefDepartmentValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChiefDepartmentValidationServiceImpl implements ChiefDepartmentValidationService {

    private final Map<Integer, String> chiefDepartmentRegistry;
    
    @Override
    public boolean isValidChiefDepartmentMatricule(Integer matricule) {
        boolean isValid = chiefDepartmentRegistry.containsKey(matricule);
        log.info("ChiefDepartment matricule validation: {} -> {}", matricule, isValid ? "valid" : "invalid");
        return isValid;
    }
    
    @Override
    public boolean validateChiefDepartmentIdentity(Integer matricule, String name) {
        if (!chiefDepartmentRegistry.containsKey(matricule)) {
            return false;
        }
        
        String registeredName = chiefDepartmentRegistry.get(matricule);
        return registeredName.equalsIgnoreCase(name);
    }
}