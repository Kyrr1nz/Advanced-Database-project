package com.example.student_management.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "exam")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exam_id;

    @Column(name = "exam_date")
    private LocalDate examDate;

    private String room;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private CourseSection courseSection;

    @ManyToOne
    @JoinColumn(name = "sid") // Khớp với SID của Supervisor trong ERD
    private Supervisor supervisor;

    // Getter & Setter
    public Long getExamId() { return exam_id; }
    public void setExamId(Long exam_id) { this.exam_id = exam_id; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public CourseSection getCourseSection() { return courseSection; }
    public void setCourseSection(CourseSection courseSection) { this.courseSection = courseSection; }

    public Supervisor getSupervisor() { return supervisor; }
    public void setSupervisor(Supervisor supervisor) { this.supervisor = supervisor; }
}