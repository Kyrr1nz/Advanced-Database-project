package com.example.student_management.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacher_id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String email;
    private String phone;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CourseSection> courseSections;

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Supervisor supervisor;

    // Getter & Setter
    public Long getId() { return teacher_id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}