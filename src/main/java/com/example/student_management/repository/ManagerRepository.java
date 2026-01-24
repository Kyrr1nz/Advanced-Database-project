package com.example.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.student_management.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
