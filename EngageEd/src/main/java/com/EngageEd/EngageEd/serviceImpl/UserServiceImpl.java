package com.EngageEd.EngageEd.serviceImpl;

import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.dto.mapper.UserMapper;
import com.EngageEd.EngageEd.dto.user.UserRegistrationDTO;
import com.EngageEd.EngageEd.dto.user.UserResponseDTO;
import com.EngageEd.EngageEd.model.DepartmentChief;
import com.EngageEd.EngageEd.model.Professor;
import com.EngageEd.EngageEd.model.Student;
import com.EngageEd.EngageEd.model.User;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.repository.DepartmentChiefRepository;
import com.EngageEd.EngageEd.repository.ProfessorRepository;
import com.EngageEd.EngageEd.repository.StudentRepository;
import com.EngageEd.EngageEd.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final StudentRepository studentRepository;
    private final DepartmentChiefRepository departmentChiefRepository;
    private final ProfessorRepository professorRepository;

    public UserServiceImpl(StudentRepository studentRepository,
                          DepartmentChiefRepository departmentChiefRepository, 
                          ProfessorRepository professorRepository) {
        this.studentRepository = studentRepository;
        this.departmentChiefRepository = departmentChiefRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public void createUser(String email, String fullname, String password, UserRole role) {
        // Delegate to the DTO version
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setEmail(email);
        registrationDTO.setFullName(fullname);
        registrationDTO.setPassword(password);
        registrationDTO.setRole(role);
        
        createUser(registrationDTO);
    }
    
    @Override
    public UserResponseDTO createUser(UserRegistrationDTO registrationDTO) {
        UserRole userRole = registrationDTO.getRole();
        User savedUser;

        if (userRole == UserRole.STUDENT) {
            // Create a new student
            Student student = new Student();
            setupUserFields(student, registrationDTO);
            savedUser = studentRepository.save(student);
            
        } else if (userRole == UserRole.PROFESSOR) {
            // Create a new professor
            Professor professor = new Professor();
            setupUserFields(professor, registrationDTO);
            savedUser = professorRepository.save(professor);
            
        } else if (userRole == UserRole.DEPARTMENT_CHIEF) {
            // Create a new department chief
            DepartmentChief departmentChief = new DepartmentChief();
            setupUserFields(departmentChief, registrationDTO);
            savedUser = departmentChiefRepository.save(departmentChief);
            
        } else {
            throw new IllegalArgumentException("Unsupported user role: " + userRole);
        }
        
        // Convert to response DTO using the mapper
        return UserMapper.toDTO(savedUser);
    }
    
    /**
     * Helper method to set common user fields
     */
    private void setupUserFields(User user, UserRegistrationDTO dto) {
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
    }
}
