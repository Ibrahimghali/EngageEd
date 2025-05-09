package com.EngageEd.EngageEd.service;

import com.EngageEd.EngageEd.dto.AuthenticationDTOs;
import com.EngageEd.EngageEd.model.User;
import com.EngageEd.EngageEd.model.UserRole;

public interface FirebaseAuthService {
    String verifyFirebaseToken(String token);
    AuthenticationDTOs.LoginResponse authenticateWithFirebaseToken(
            AuthenticationDTOs.FirebaseAuthRequest request);
    String createFirebaseUser(String email, String password, String displayName, UserRole role);
    void updateFirebaseUserClaims(User user);
    void deleteFirebaseUser(String firebaseUid);
    String generatePasswordResetLink(String email);
    String generateEmailSignInLink(String email, String redirectUrl);
    String createUser(String email, String password);
    void deleteUser(String firebaseUid) throws Exception;
}
