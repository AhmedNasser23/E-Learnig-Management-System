package com.project.LMS.entity;

import com.project.LMS.entity.users.Student;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Grades")
public class Grade {
    @Id
    private Long id;
    private Double score;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
