package com.project.LMS.dto.assignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentsGradesDto {
    private String assignmentName;
    private Double assignmentGrade;
}
