package com.example.student_management.entity;

import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;

@Entity
@Table(
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(length = 10)
    private String gender;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    // =========================
    // Student <-> Subject (N-N)
    // join table: SIGN
    // =========================
    @ManyToMany
    @JoinTable(
            name = "sign",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    // ===== Constructors =====
    protected Student() {}

    public Student(
            String fullName,
            String email,
            LocalDate dob,
            String gender,
            String phoneNumber
    ) {
        this.fullName = fullName;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    // ===== Getter & Setter =====
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }
}
