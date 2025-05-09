package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.EngageEd.EngageEd.dto.PageResponse;
import com.EngageEd.EngageEd.dto.StudentDTOs;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.dto.AuthenticationDTOs;

public interface StudentService {
    Student createStudent(StudentDTOs.StudentRegistrationRequest request);
    StudentDTOs.StudentResponse getStudentById(UUID id);
    StudentDTOs.StudentResponse getStudentByEmail(String email);
    StudentDTOs.StudentResponse getStudentByFirebaseUid(String firebaseUid);
    Optional<Student> findStudentByFirebaseUid(String firebaseUid);
    Student getStudentEntityById(UUID id);
    List<StudentDTOs.StudentResponse> getAllStudents();
    List<StudentDTOs.StudentResponse> getStudentsBySubject(UUID subjectId);
    PageResponse<StudentDTOs.StudentResponse> getStudentsPaged(int page, int size);
    StudentDTOs.StudentResponse updateStudent(UUID id, StudentDTOs.StudentUpdateRequest request);
    void deleteStudent(UUID id);
    Student findStudentEntityByEmail(String email);
    Student createStudent(AuthenticationDTOs.RegistrationRequest request, String firebaseUid);
}
