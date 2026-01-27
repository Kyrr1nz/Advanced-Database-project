package com.example.student_management.service;

import com.example.student_management.entity.Teacher;
import com.example.student_management.repository.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    private final TeacherRepository repository;

    public TeacherService(TeacherRepository repository) {
        this.repository = repository;
    }
}
