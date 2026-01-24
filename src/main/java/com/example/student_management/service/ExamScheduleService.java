package com.example.student_management.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.student_management.entity.ExamSchedule;
import com.example.student_management.repository.ExamScheduleRepository;

@Service
public class ExamScheduleService {

    private final ExamScheduleRepository examScheduleRepository;

    public ExamScheduleService(ExamScheduleRepository examScheduleRepository) {
        this.examScheduleRepository = examScheduleRepository;
    }

    public List<ExamSchedule> findAll() {
        return examScheduleRepository.findAll();
    }

    public ExamSchedule save(ExamSchedule examSchedule) {
        return examScheduleRepository.save(examSchedule);
    }

    public void deleteById(Long id) {
        examScheduleRepository.deleteById(id);
    }
}
