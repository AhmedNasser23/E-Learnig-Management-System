package com.project.LMS.dto.assignment;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssignmentSubmissionRequestBody {
    private int assignmentId;
    private int studentId;
    private String fileURL;
}
