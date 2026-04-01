package com.example.student_management.repository;

import com.example.student_management.entity.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    List<CourseSection> findByStartDateAfter(LocalDate date);

    @Query("SELECT cs FROM CourseSection cs " +
            "LEFT JOIN FETCH cs.subject " +
            "LEFT JOIN FETCH cs.teacher " +
            "LEFT JOIN FETCH cs.enrollments en " +
            "LEFT JOIN FETCH en.student s " +
            "LEFT JOIN FETCH s.clazz " +
            "WHERE cs.course_section_id = :id")
    Optional<CourseSection> findByIdWithStudents(@Param("id") Long id);
}