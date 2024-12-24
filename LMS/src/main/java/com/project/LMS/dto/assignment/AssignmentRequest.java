package com.project.LMS.dto.assignment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentRequest {
    private String title;
    private int courseId;
}
