package com.example.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.student_management.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
