package com.project.LMS.dao.Assignments;

import com.project.LMS.dto.assignment.AssignmentSubmissionRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentSubmissionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final String INSERT_ASSIGNMENT_SUBMISSION_SQL = "INSERT INTO AssignmentSubmissions (assignment_id, student_id, fileUrl) VALUES ( ?, ?, ?)";

    public void submitAssignment(AssignmentSubmissionRequestBody body) {
        jdbcTemplate.update(INSERT_ASSIGNMENT_SUBMISSION_SQL, body.getAssignmentId(), body.getStudentId(), body.getFileURL());
    }

}
