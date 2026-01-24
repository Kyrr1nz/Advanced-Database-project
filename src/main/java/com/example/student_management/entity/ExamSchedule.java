package com.example.student_management.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_schedule")
public class ExamSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "room", length = 50)
    private String room;

    // =========================
    // ExamSchedule -> Manager (N-1)
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid", nullable = false)
    private Manager manager;

    // =========================
    // ExamSchedule -> Subject (N-1)
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // ===== Constructors =====
    protected ExamSchedule() {
    }

    public ExamSchedule(
            LocalDate examDate,
            LocalTime startTime,
            LocalTime endTime,
            String room,
            Manager manager,
            Subject subject
    ) {
        this.examDate = examDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.manager = manager;
        this.subject = subject;
    }

    // ===== Getter & Setter =====
    public Long getId() {
        return id;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
