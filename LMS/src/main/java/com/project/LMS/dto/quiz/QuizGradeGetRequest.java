package com.project.LMS.dto.quiz;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class QuizGradeGetRequest {
    private String studentName;
    private Double quizGrade;
}
