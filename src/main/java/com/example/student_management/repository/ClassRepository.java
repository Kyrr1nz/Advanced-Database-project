package com.example.student_management.repository;

import com.example.student_management.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByClassNameContainingIgnoreCase(String className);

    @Query("SELECT c FROM ClassEntity c " +
            "LEFT JOIN FETCH c.major " +
            "LEFT JOIN FETCH c.students " +
            "WHERE c.id = :id")
    Optional<ClassEntity> findByIdWithStudents(@Param("id") Long id);
}
