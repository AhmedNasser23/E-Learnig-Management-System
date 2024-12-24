package com.project.LMS.dto.quiz;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizGradeRequest {
    private int gradeId;
    private int quizId;
    private int studentId;
    private int score;
}
