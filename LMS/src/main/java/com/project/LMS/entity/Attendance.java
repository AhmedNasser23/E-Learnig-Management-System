package com.project.LMS.entity;
import com.project.LMS.entity.users.Student;
import javax.persistence.*;

@Entity
public class Attendance {
    @EmbeddedId
    private StudentLessonKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("lessonId")
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;


    public void setId (StudentLessonKey id) {
        this.id = id;
    }
    public StudentLessonKey getId() {
        return id;
    }
    public void setStudent (Student student) {
        this.student = student;
    }
    public Student getStudent() {
        return student;
    }
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
    public Lesson getLesson() {
        return lesson;
    }


}