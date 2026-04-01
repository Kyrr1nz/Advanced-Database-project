package com.example.student_management.repository;

import com.example.student_management.entity.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {
    //tìm kiếm theo tên giám thị nếu cần
    java.util.List<Supervisor> findBysNameContaining(String name);

    // Tìm theo tên (sName) hoặc mã số (sNumber)
    List<Supervisor> findBysNameContainingIgnoreCaseOrSNumberContainingIgnoreCase(String name, String sNumber);
}