package com.project.LMS.dto.enrollment;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnrollmentRequest {
    private int studentId;
    private int courseId;
}