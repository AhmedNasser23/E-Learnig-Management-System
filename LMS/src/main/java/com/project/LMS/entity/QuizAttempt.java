package com.project.LMS.entity;

import com.project.LMS.entity.users.Student;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Quiz_Attempts")
@Getter
@Setter
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private Double score;

    private LocalDateTime attemptDate;
}
