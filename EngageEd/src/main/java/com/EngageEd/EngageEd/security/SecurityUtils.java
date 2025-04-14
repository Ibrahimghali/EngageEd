package com.EngageEd.EngageEd.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    /**
     * Get the Firebase UID of the currently authenticated user
     * 
     * @return An Optional containing the Firebase UID if available
     */
    public static Optional<String> getCurrentUserFirebaseUid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && 
                authentication.getPrincipal() instanceof FirebaseUserDetails) {
            FirebaseUserDetails userDetails = (FirebaseUserDetails) authentication.getPrincipal();
            return Optional.ofNullable(userDetails.getUid());
        }
        
        return Optional.empty();
    }
    
    /**
     * Get the email of the currently authenticated user
     * 
     * @return An Optional containing the email if available
     */
    public static Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && 
                authentication.getPrincipal() instanceof FirebaseUserDetails) {
            FirebaseUserDetails userDetails = (FirebaseUserDetails) authentication.getPrincipal();
            return Optional.ofNullable(userDetails.getEmail());
        }
        
        return Optional.empty();
    }
    
    /**
     * Check if the current user has a specific authority
     * 
     * @param authority The authority to check for
     * @return true if the user has the authority, false otherwise
     */
    public static boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(authority));
        }
        
        return false;
    }
}