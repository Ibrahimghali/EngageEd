package com.EngageEd.EngageEd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Add this annotation to enable the builder pattern
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String subjectCode;
    
    @Column(length = 1000)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Professor creator;
    
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @Builder.Default // Add this to initialize collections in builder
    private Set<Material> materials = new HashSet<>();
    
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @Builder.Default // Add this to initialize collections in builder
    private Set<Enrollment> enrollments = new HashSet<>();
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(nullable = false)
    @Builder.Default // Add this to use the default value in builder
    private boolean active = true;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        
        // Generate a random subject code if not set
        if (subjectCode == null || subjectCode.isEmpty()) {
            // Simple implementation - in real app you'd want to ensure uniqueness
            subjectCode = generateSubjectCode();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    private String generateSubjectCode() {
        // Simple implementation - in production you'd want a more robust approach
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}