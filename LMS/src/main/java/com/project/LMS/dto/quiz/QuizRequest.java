package com.project.LMS.dto.quiz;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class QuizRequest {
    private int id;
    private LocalDate date;
    private String title;
    private int courseId;
}
