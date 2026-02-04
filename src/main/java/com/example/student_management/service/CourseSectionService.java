package com.example.student_management.service;

import com.example.student_management.entity.CourseSection;
import com.example.student_management.entity.Subject;
import com.example.student_management.repository.CourseSectionRepository;
import com.example.student_management.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseSectionService {

    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    // Lấy tất cả các lớp
    public List<CourseSection> findAllSections() {
        return courseSectionRepository.findAll();
    }

    // MỚI: Lọc các lớp chưa bắt đầu học (Logic nghiệp vụ)
    public List<CourseSection> findAvailableSections() {
        LocalDate today = LocalDate.now();
        return courseSectionRepository.findAll().stream()
                .filter(section -> section.getStartDate() != null && section.getStartDate().isAfter(today))
                .collect(Collectors.toList());
    }

    public long countSections() {
        return courseSectionRepository.count();
    }

    public long countSubjects() {
        return subjectRepository.count();
    }

    public List<CourseSection> findSectionsStartingAfter(LocalDate date) {
        return courseSectionRepository.findByStartDateAfter(date);
    }
}