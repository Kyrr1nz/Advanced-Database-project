package com.example.student_management.service;

import com.example.student_management.entity.Supervisor;
import com.example.student_management.repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupervisorService {

    @Autowired
    private SupervisorRepository supervisorRepository;

    public List<Supervisor> findAll() {
        return supervisorRepository.findAll();
    }

    public void save(Supervisor supervisor) {
        if (supervisor != null) {
            supervisorRepository.save(supervisor);
        }
    }

    public void delete(Supervisor supervisor) {
        supervisorRepository.delete(supervisor);
    }

    // MỚI: Thêm hàm search cho Supervisor
    public List<Supervisor> search(String filter) {
        if (filter == null || filter.isEmpty()) {
            return supervisorRepository.findAll();
        }
        return supervisorRepository.findBysNameContainingIgnoreCaseOrSNumberContainingIgnoreCase(filter, filter);
    }
}