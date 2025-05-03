package com.EngageEd.EngageEd.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.EngageEd.EngageEd.dto.ApiResponse;
import com.EngageEd.EngageEd.dto.UserDTOs;
import com.EngageEd.EngageEd.exception.ResourceNotFoundException;
import com.EngageEd.EngageEd.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final FirebaseAuth firebaseAuth;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String firebaseToken = authHeader.substring(7); // Remove "Bearer " prefix
        
        try {
            // Verify Firebase token directly
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(firebaseToken);
            String firebaseUid = decodedToken.getUid();
            
            if (firebaseUid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    // Get user from our database
                    UserDTOs.UserResponse user = userService.getUserByFirebaseUid(firebaseUid);
                    
                    // Set authentication
                    List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole())
                    );
                    
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        authorities
                    );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                } catch (ResourceNotFoundException e) {
                    log.warn("Attempted access with deleted user account, Firebase UID: {}", firebaseUid);
                    sendDeletedUserResponse(response);
                    return; // Stop filter chain here - important!
                }
            }
        } catch (FirebaseAuthException e) {
            log.error("Firebase authentication failed", e);
            // Invalid token - don't set authentication
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Sends a 401 Unauthorized response with a message indicating the user account has been deleted
     */
    private void sendDeletedUserResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        
        ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                .success(false)
                .message("Your account has been deleted. Please create a new account or contact support.")
                .timestamp(LocalDateTime.now())
                .build();
        
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}