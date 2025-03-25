package com.EngageEd.EngageEd.serviceImpl;

import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.entity.Subject;
import com.EngageEd.EngageEd.repository.SubjectRepo;

@Service
public class SubjectService {
    private final SubjectRepo subjectRepository;

    // @Autowired
    public SubjectService(SubjectRepo subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<SubjectRepo> getSubjectsByDepartmentChief(Long chiefId) {
        return subjectRepository.findByCreatedBy(chiefId);
    }

    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }
}
