package com.example.student_management.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate startDate;
    private LocalDate dueDate;

    // =========================
    // Manager <-> Student (N-N)
    // join table: manage_student
    // =========================
    @ManyToMany
    @JoinTable(
            name = "manage_student",
            joinColumns = @JoinColumn(name = "manager_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    // =========================
    // Manager -> Class (1-N)
    // =========================
    @OneToMany(mappedBy = "manager")
    private List<ClassEntity> classes;

    // =========================
    // Manager -> ExamSchedule (1-N)
    // =========================
    @OneToMany(mappedBy = "manager")
    private List<ExamSchedule> examSchedules;

    // ===== Constructors =====
    protected Manager() {
    }

    public Manager(
            String fullName,
            String email,
            LocalDate startDate,
            LocalDate dueDate
    ) {
        this.fullName = fullName;
        this.email = email;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    // ===== Getter & Setter =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<ClassEntity> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassEntity> classes) {
        this.classes = classes;
    }

    public List<ExamSchedule> getExamSchedules() {
        return examSchedules;
    }

    public void setExamSchedules(List<ExamSchedule> examSchedules) {
        this.examSchedules = examSchedules;
    }
}
