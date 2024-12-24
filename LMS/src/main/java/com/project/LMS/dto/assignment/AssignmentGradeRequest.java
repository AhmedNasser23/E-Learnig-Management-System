package com.project.LMS.dto.assignment;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssignmentGradeRequest {
    private int gradeId;
    private int assignmentId;
    private int studentId;
    private int score;
}
