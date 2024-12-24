package com.project.LMS.entity;


import com.project.LMS.entity.users.Student;

import javax.persistence.*;

@Entity
public class Enrollment {
    @EmbeddedId
    private StudentCourseKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    // Getters and setters
    public StudentCourseKey getId() {
        return id;
    }

    public void setId(StudentCourseKey id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}