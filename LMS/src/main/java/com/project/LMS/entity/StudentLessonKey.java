package com.project.LMS.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
@Embeddable
public class StudentLessonKey implements Serializable {
    private Integer studentId;
    private Integer lessonId;

    public StudentLessonKey() {}

    public StudentLessonKey(Integer studentId, Integer lessonId) {
        this.studentId = studentId;
        this.lessonId = lessonId;
    }

    // Getters and setters
    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentLessonKey that = (StudentLessonKey) o;
        return Objects.equals(studentId, that.studentId) &&
                Objects.equals(lessonId, that.lessonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, lessonId);
    }
}