package com.app.online_submission.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "assignment_id")
    private int assignmentId;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "grade")
    private int grade;

    // Constructor, Getters and Setters

    public Grade() {}

    public Grade(int assignmentId, int studentId, int grade) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
