package com.EngageEd.EngageEd.service;

public interface ProfessorValidationService {
    /**
     * Validates if the given matricule belongs to an authorized professor
     * 
     * @param matricule The matricule to validate
     * @return true if the matricule is valid, false otherwise
     */
    boolean isValidProfessorMatricule(Integer matricule);
    
    /**
     * Validates if the given matricule and name match a registered professor
     * 
     * @param matricule The matricule to validate
     * @param name The name to validate against the registry
     * @return true if both matricule and name match, false otherwise
     */
    boolean validateProfessorIdentity(Integer matricule, String name);
}
