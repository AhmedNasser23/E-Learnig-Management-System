package com.project.LMS.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Quizzes")
public class Quiz {
    @Id
    private Integer id;

    @Column(name = "title")
    private String title;
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Grade> grades;

    public Quiz() {

    }

    public void setCourseId(int courseId) {
        this.course = new Course();
        this.course.setId(courseId);
    }

    public Quiz(int id, String title, int courseId) {
        this.id = id;
        this.title = title;
    }
}
