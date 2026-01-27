package com.example.student_management.service;

import com.example.student_management.entity.CourseSection;
import com.example.student_management.entity.Subject;
import com.example.student_management.repository.CourseSectionRepository;
import com.example.student_management.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CourseSectionService {

    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<CourseSection> findAllSections() {
        return courseSectionRepository.findAll();
    }

    public long countSubjects() {
        return subjectRepository.count();
    }
}