package com.project.LMS.entity.users;

import com.project.LMS.entity.Course;
import com.project.LMS.entity.InstructorNotification;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "Instructors")
public class Instructor extends User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<InstructorNotification> notifications;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Course> courses;
}