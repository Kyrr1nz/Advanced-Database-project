package com.example.student_management.service;

import com.example.student_management.entity.Subject;
import com.example.student_management.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository repository;

    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }

    public List<Subject> findAll() {
        return repository.findAll();
    }
}
