package com.example.student_management.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.student_management.entity.ClassEntity;
import com.example.student_management.repository.ClassRepository;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public List<ClassEntity> findAll() {
        return classRepository.findAll();
    }

    public ClassEntity save(ClassEntity classEntity) {
        return classRepository.save(classEntity);
    }

    public void deleteById(Long id) {
        classRepository.deleteById(id);
    }
}
