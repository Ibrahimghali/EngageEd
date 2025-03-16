package com.EngageEd.EngageEd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.dto.Enrollment;
import com.EngageEd.EngageEd.repository.EnrollmentRepo;

@Service
public class EnrollmentService {
    private final EnrollmentRepo enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepo enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> getEnrollmentsByUser(Long userId) {
        return enrollmentRepository.findByUserId(userId);
    }

    public List<Enrollment> getEnrollmentsBySubject(Long subjectId) {
        return enrollmentRepository.findBySubjectId(subjectId);
    }

    public Enrollment enrollStudent(Long userId, Long subjectId) {
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setSubjectId(subjectId);
        return enrollmentRepository.save(enrollment);
    }
}
