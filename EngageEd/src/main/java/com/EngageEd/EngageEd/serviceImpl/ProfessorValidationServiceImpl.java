package com.EngageEd.EngageEd.serviceImpl;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import com.EngageEd.EngageEd.service.ProfessorValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfessorValidationServiceImpl implements ProfessorValidationService {

    private final Map<Integer, String> professorRegistry;
    
    @Override
    public boolean isValidProfessorMatricule(Integer matricule) {
        boolean isValid = professorRegistry.containsKey(matricule);
        log.info("Professor matricule validation: {} -> {}", matricule, isValid ? "valid" : "invalid");
        return isValid;
    }
    
    @Override
    public boolean validateProfessorIdentity(Integer matricule, String name) {
        if (!professorRegistry.containsKey(matricule)) {
            return false;
        }
        
        String registeredName = professorRegistry.get(matricule);
        return registeredName.equalsIgnoreCase(name);
    }
}