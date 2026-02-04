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
    private com.example.student_management.entity.CourseSection courseSection;

    // Getter & Setter
    public LocalDate getExamDate() { return examDate; }
    public String getRoom() { return room; }
    public CourseSection getCourseSection() {return courseSection;}
    public void setCourseSection(com.example.student_management.entity.CourseSection courseSection) { this.courseSection = courseSection; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }
    public void setRoom(String room) { this.room = room; }

}