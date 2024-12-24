package com.project.LMS.entity.users;
import com.project.LMS.entity.Course;
import com.project.LMS.entity.Grade;
import com.project.LMS.entity.StudentNotification;
import com.project.LMS.entity.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "Students")
public class Student extends User {
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Grade> grades;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentNotification> studentNotifications;

    ////////////////////////////////////////////////////////////////////
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances ;
}