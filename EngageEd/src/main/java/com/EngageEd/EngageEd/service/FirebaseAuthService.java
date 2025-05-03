package com.EngageEd.EngageEd.service;

import com.EngageEd.EngageEd.dto.AuthenticationDTOs;
import com.EngageEd.EngageEd.model.User;
import com.EngageEd.EngageEd.model.UserRole;

/**
 * Service interface for Firebase authentication operations
 */
public interface FirebaseAuthService {
    
    /**
     * Verify a Firebase ID token
     * 
     * @param token The Firebase ID token
     * @return The UID extracted from the token
     */
    String verifyFirebaseToken(String token);
    
    /**
     * Authenticate a user with Firebase token
     * 
     * @param request The authentication request containing Firebase token
     * @return The login response with JWT token and user info
     */
    AuthenticationDTOs.LoginResponse authenticateWithFirebaseToken(
            AuthenticationDTOs.FirebaseAuthRequest request);
    
    /**
     * Create a new user in Firebase
     * 
     * @param email The user's email
     * @param password The initial password (optional)
     * @param displayName The user's display name
     * @param role The user's role
     * @return The Firebase UID of the created user
     */
    String createFirebaseUser(String email, String password, String displayName, UserRole role);
    
    /**
     * Update Firebase user claims based on our application user
     * 
     * @param user The application user
     */
    void updateFirebaseUserClaims(User user);
    
    /**
     * Delete a user from Firebase
     * 
     * @param firebaseUid The Firebase UID
     */
    void deleteFirebaseUser(String firebaseUid);
    
    /**
     * Generate a password reset link
     * 
     * @param email The user's email
     * @return The password reset link
     */
    String generatePasswordResetLink(String email);
    
    /**
     * Generate a custom sign-in link that can be used for email invitations
     * 
     * @param email The user's email
     * @param redirectUrl The URL to redirect to after sign-in
     * @return The sign-in link
     */
    String generateEmailSignInLink(String email, String redirectUrl);
    
    /**
     * Assign a role to a user
     * 
     * @param uid The Firebase UID
     * @param role The role to assign
     */

    /**
     * Creates a new user in Firebase
     * 
     * @param email The user's email
     * @param password The user's password
     * @return The Firebase UID of the created user
     */
    String createUser(String email, String password);

    /**
     * Deletes a user from Firebase Authentication by their UID
     * 
     * @param firebaseUid the unique identifier of the user in Firebase
     * @throws Exception if the deletion fails
     */
    void deleteUser(String firebaseUid) throws Exception;
}