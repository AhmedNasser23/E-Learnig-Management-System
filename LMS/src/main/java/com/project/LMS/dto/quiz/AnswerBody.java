package com.project.LMS.dto.quiz;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnswerBody {
    private String studentAnswer;
    private Integer attempt_id;
    private Integer question_id;
}
