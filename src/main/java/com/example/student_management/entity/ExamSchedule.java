package com.example.student_management.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;

@Entity
@Table(name = "exam_schedule")
public class ExamSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long esid;

    @Column(nullable = false, unique = true)
    private String esName;

    private LocalDate esDate;
    private LocalTime esTime;

    private String esCourse;

    @ManyToOne
    @JoinColumn(name = "mid")
    private Manager manager;

    // Constructors
    public ExamSchedule() {}

    // Getter & Setter
    public Long getEsid() {
        return esid;
    }

    public void setEsid(Long esid) {
        this.esid = esid;
    }

    public String getEsName() {
        return esName;
    }

    public void setEsName(String esName) {
        this.esName = esName;
    }

    public LocalDate getEsDate() {
        return esDate;
    }

    public void setEsDate(LocalDate esDate) {
        this.esDate = esDate;
    }

    public LocalTime getEsTime() {
        return esTime;
    }

    public void setEsTime(LocalTime esTime) {
        this.esTime = esTime;
    }

    public String getEsCourse() {
        return esCourse;
    }

    public void setEsCourse(String esCourse) {
        this.esCourse = esCourse;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
