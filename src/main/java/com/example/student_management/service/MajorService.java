package com.example.student_management.service;

import com.example.student_management.entity.Major;
import com.example.student_management.repository.MajorRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class MajorService {

    @Autowired
    private MajorRepository majorRepository;

    public List<Major> findAll() {
        return majorRepository.findAll();
    }

    public long countMajors() {
        return majorRepository.count();
    }
}