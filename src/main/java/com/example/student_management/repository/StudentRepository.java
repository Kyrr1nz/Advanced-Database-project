package com.example.student_management.repository;

import com.example.student_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Lấy thông tin kèm Major và Class để hiển thị Layer 1 (thân thiện)
    @Query("SELECT s FROM Student s " +
            "LEFT JOIN FETCH s.clazz c " +
            "LEFT JOIN FETCH c.major m")
    List<Student> findAllWithBasicInfo();
}