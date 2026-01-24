package com.example.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.student_management.entity.ClassEntity;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
}
