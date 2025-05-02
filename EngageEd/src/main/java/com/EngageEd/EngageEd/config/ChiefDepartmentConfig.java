package com.EngageEd.EngageEd.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ChiefDepartmentConfig {
    
    @Bean
    public Map<Integer, String> chiefDepartmentRegistry() {
        Map<Integer, String> registry = new HashMap<>();
        // Only one person allowed as ChiefDepartment
        registry.put(2001, "James Wilson");
        
        log.info("ChiefDepartment registry initialized with {} entries", registry.size());
        return registry;
    }
}