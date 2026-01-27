package com.example.student_management.service;

import com.example.student_management.entity.ClassEntity;
import com.example.student_management.repository.ClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import java.util.List;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    public List<ClassEntity> findAll() {
        return classRepository.findAll();
    }

    public long countClasses() {
        return classRepository.count();
    }

    public Optional<ClassEntity> findById(Long id) {
        return classRepository.findById(id);
    }
}