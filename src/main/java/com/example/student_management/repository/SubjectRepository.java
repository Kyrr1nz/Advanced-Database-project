package com.example.student_management.repository;

import com.example.student_management.entity.ClassEntity;
import com.example.student_management.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findBySubjectNameContainingIgnoreCase(String subjectName);
}