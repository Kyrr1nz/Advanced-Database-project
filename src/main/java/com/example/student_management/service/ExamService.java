package com.example.student_management.service;

import com.example.student_management.entity.Exam;
import com.example.student_management.repository.ExamRepository;
import org.springframework.stereotype.Service;

@Service
public class ExamService {

    private final ExamRepository repository;

    public ExamService(ExamRepository repository) {
        this.repository = repository;
    }
}
