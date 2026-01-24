package com.example.student_management.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;

    @Column(name = "m_full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "m_email", nullable = false, unique = true, length = 100)
    private String email;

    private LocalDate mStartDate;
    private LocalDate mDueDate;

    // Constructors
    public Manager() {}

    // Getter & Setter
    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
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

    public LocalDate getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(LocalDate mStartDate) {
        this.mStartDate = mStartDate;
    }

    public LocalDate getmDueDate() {
        return mDueDate;
    }

    public void setmDueDate(LocalDate mDueDate) {
        this.mDueDate = mDueDate;
    }
}
