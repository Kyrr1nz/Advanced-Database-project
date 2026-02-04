package com.example.student_management.repository;

import com.example.student_management.entity.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    List<CourseSection> findByStartDateAfter(LocalDate date);
}