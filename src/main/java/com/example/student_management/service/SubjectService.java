package com.example.student_management.service;

import com.example.student_management.entity.ClassEntity;
import com.example.student_management.entity.Subject;
import com.example.student_management.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository repository) {
        this.subjectRepository= repository;
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public List<Subject> search(String keyword) {
        return subjectRepository.findBySubjectNameContainingIgnoreCase(keyword);
    }
}
