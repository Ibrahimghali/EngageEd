package com.EngageEd.EngageEd.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.dto.AuthenticationDTOs;
import com.EngageEd.EngageEd.dto.UserDTOs;
import com.EngageEd.EngageEd.exception.AuthenticationException;
import com.EngageEd.EngageEd.model.User;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.service.FirebaseAuthService;
import com.EngageEd.EngageEd.service.UserService;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseAuthServiceImpl implements FirebaseAuthService {

    private final UserService userService;
    private final FirebaseAuth firebaseAuth;
    
    @Override
    public String verifyFirebaseToken(String token) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
            return decodedToken.getUid();
        } catch (FirebaseAuthException e) {
            log.error("Error verifying Firebase token", e);
            throw new AuthenticationException("Invalid Firebase token");
        }
    }

    @Override
    public AuthenticationDTOs.LoginResponse authenticateWithFirebaseToken(AuthenticationDTOs.FirebaseAuthRequest request) {
        try {
            // Verify the token with Firebase
            String firebaseUid = verifyFirebaseToken(request.getFirebaseToken());
            
            // Get user from our database
            UserDTOs.UserResponse user = userService.getUserByFirebaseUid(firebaseUid);
            
            // Return the Firebase token directly - no need to generate a JWT
            return AuthenticationDTOs.LoginResponse.builder()
                    .jwtToken(request.getFirebaseToken())  // Use Firebase token directly
                    .refreshToken(null)                     // No separate refresh token needed
                    .user(user)
                    .build();
        } catch (Exception e) {
            log.error("Error authenticating user with Firebase", e);
            throw new AuthenticationException("Error authenticating user: " + e.getMessage());
        }
    }

    @Override
    public String createFirebaseUser(String email, String password, String displayName, UserRole role) {
        try {
            CreateRequest request = new CreateRequest()
                    .setEmail(email)
                    .setDisplayName(displayName)
                    .setEmailVerified(true);
            
            if (password != null && !password.isEmpty()) {
                request.setPassword(password);
            }
            
            UserRecord userRecord = firebaseAuth.createUser(request);
            
            // Set custom claims for role-based access
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", role.toString());
            firebaseAuth.setCustomUserClaims(userRecord.getUid(), claims);
            
            return userRecord.getUid();
        } catch (FirebaseAuthException e) {
            log.error("Error creating Firebase user", e);
            throw new AuthenticationException("Error creating Firebase user: " + e.getMessage());
        }
    }

    @Override
    public void updateFirebaseUserClaims(User user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().toString());
            claims.put("userId", user.getId().toString());
            
            firebaseAuth.setCustomUserClaims(user.getFirebaseUid(), claims);
        } catch (FirebaseAuthException e) {
            log.error("Error updating Firebase user claims", e);
            throw new AuthenticationException("Error updating Firebase user claims: " + e.getMessage());
        }
    }

    @Override
    public void deleteFirebaseUser(String firebaseUid) {
        try {
            firebaseAuth.deleteUser(firebaseUid);
        } catch (FirebaseAuthException e) {
            log.error("Error deleting Firebase user", e);
            throw new AuthenticationException("Error deleting Firebase user: " + e.getMessage());
        }
    }

    @Override
    public String generatePasswordResetLink(String email) {
        try {
            return firebaseAuth.generatePasswordResetLink(email);
        } catch (FirebaseAuthException e) {
            log.error("Error generating password reset link", e);
            throw new AuthenticationException("Error generating password reset link: " + e.getMessage());
        }
    }

    @Override
    public String generateEmailSignInLink(String email, String redirectUrl) {
        try {
            ActionCodeSettings actionCodeSettings = ActionCodeSettings.builder()
                .setUrl(redirectUrl)
                .setHandleCodeInApp(true)  // Optional: handle code in app
                .build();
            
            return firebaseAuth.generateSignInWithEmailLink(email, actionCodeSettings);
        } catch (FirebaseAuthException e) {
            log.error("Error generating email sign-in link", e);
            throw new AuthenticationException("Error generating email sign-in link: " + e.getMessage());
        }
    }

    @Override
    public String createUser(String email, String password) {
        log.info("Creating Firebase user with email: {}", email);
        
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setEmailVerified(false);
            
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            return userRecord.getUid();
        } catch (FirebaseAuthException e) {
            log.error("Error creating Firebase user: {}", e.getMessage());
            throw new RuntimeException("Error creating user in Firebase: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String firebaseUid) throws Exception {
        try {
            FirebaseAuth.getInstance().deleteUser(firebaseUid);
            log.info("Successfully deleted user with UID: {}", firebaseUid);
        } catch (FirebaseAuthException e) {
            log.error("Error deleting Firebase user: {}", e.getMessage());
            throw new RuntimeException("Error deleting user in Firebase: " + e.getMessage(), e);
        }
    }

}