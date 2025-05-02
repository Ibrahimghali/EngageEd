package com.EngageEd.EngageEd.service;

public interface ChiefDepartmentValidationService {
    boolean isValidChiefDepartmentMatricule(Integer matricule);
    boolean validateChiefDepartmentIdentity(Integer matricule, String name);
}