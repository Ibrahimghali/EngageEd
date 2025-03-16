package com.EngageEd.EngageEd.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EngageEd.EngageEd.dto.Enrollment;
import com.EngageEd.EngageEd.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/user/{userId}")
    public List<Enrollment> getEnrollmentsByUser(@PathVariable Long userId) {
        return enrollmentService.getEnrollmentsByUser(userId);
    }

    @PostMapping
    public Enrollment enrollStudent(@RequestParam Long userId, @RequestParam Long subjectId) {
        return enrollmentService.enrollStudent(userId, subjectId);
    }
}
