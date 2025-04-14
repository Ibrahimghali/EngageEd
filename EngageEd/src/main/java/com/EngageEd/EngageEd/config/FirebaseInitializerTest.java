package com.EngageEd.EngageEd.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import com.google.firebase.auth.FirebaseAuth;

@Configuration
@Slf4j
public class FirebaseInitializerTest {
    
    @Bean
    public CommandLineRunner testFirebaseConnection(FirebaseAuth firebaseAuth) {
        return args -> {
            try {
                // Just try to list users - if it works, Firebase is connected
                firebaseAuth.listUsers(null,1000);
                log.info("✅ Firebase connection successful!");
            } catch (Exception e) {
                log.error("❌ Firebase connection failed: {}", e.getMessage());
                e.printStackTrace();
            }
        };
    }
}