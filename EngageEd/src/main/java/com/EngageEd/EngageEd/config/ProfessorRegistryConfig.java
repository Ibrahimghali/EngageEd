package com.EngageEd.EngageEd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProfessorRegistryConfig {

    @Bean
    public Map<Integer, String> professorRegistry() {
        Map<Integer, String> registry = new HashMap<>();
        
        // Add authorized professor matricules with their names
        // Format: matricule -> expected name (for additional validation if needed)
        registry.put(1001, "John Doe");
        registry.put(1002, "Jane Smith");
        registry.put(1003, "Robert Johnson");
        registry.put(1004, "Emily Davis");
        registry.put(1005, "Michael Wilson");
        
        // Add more professors as needed
        
        return registry;
    }
}