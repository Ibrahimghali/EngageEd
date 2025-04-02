package com.EngageEd.EngageEd.service;

import com.EngageEd.EngageEd.model.UserRole;

public interface UserService {
    public void createUser(String email, String fullname, String password, UserRole role);
}
