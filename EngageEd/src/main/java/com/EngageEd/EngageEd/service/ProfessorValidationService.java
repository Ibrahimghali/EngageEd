package com.EngageEd.EngageEd.service;

public interface ProfessorValidationService {
    boolean isValidProfessorMatricule(Integer matricule);
    boolean validateProfessorIdentity(Integer matricule, String name);
}
