package com.EngageEd.EngageEd.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.repository.DepartmentChiefRepository;
import com.EngageEd.EngageEd.repository.ProfessorRepository;
import com.EngageEd.EngageEd.repository.StudentRepository;
import com.EngageEd.EngageEd.repository.UserRepository;
import com.EngageEd.EngageEd.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    // in the process of creating a user with specific roles
    private final StudentRepository studentRepository;
    private final DepartmentChiefRepository departmentChiefRepository;
    private final ProfessorRepository professorRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, StudentRepository studentRepository,
                           DepartmentChiefRepository departmentChiefRepository, ProfessorRepository professorRepository) {
        this.studentRepository = studentRepository;
        this.departmentChiefRepository = departmentChiefRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public void createUser(String email, String fullname, String password, UserRole role) {

        UserRole userRole = UserRole.valueOf(role.name().toUpperCase());
        if (userRole == UserRole.STUDENT) {
            // create a new student
            Student student = new Student();
            student.setEmail(email);
            student.setFullName(fullname);
            student.setPassword(password);
            student.setRole(userRole);
            // save the student to the database
            studentRepository.save(student);
        } else if (userRole == UserRole.PROFESSOR) {
            // create a new professor
            Professor professor = new Professor();
            professor.setEmail(email);
            professor.setFullName(fullname);
            professor.setPassword(password);
            professor.setRole(userRole);
            // save the professor to the database
            professorRepository.save(professor);
        } else if (userRole == UserRole.DEPARTMENT_CHIEF) {
            // create a new department chief
            DepartmentChief departmentChief = new DepartmentChief();
            departmentChief.setEmail(email);
            departmentChief.setFullName(fullname);
            departmentChief.setPassword(password);
            departmentChief.setRole(userRole);
            // save the department chief to the database
            departmentChiefRepository.save(departmentChief);
        }
    }
        

}
